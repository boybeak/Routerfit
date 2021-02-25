package com.github.boybeak.irouter.register;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class Asm {

    private static Asm sAsm = new Asm();
    public static Asm getInstance() {
        return sAsm;
    }

    private Asm(){}

    public void generateCode(File jarFile, String loaderManagerEntryName, List<String> loaders) {
        if (jarFile == null || !jarFile.exists()) {
            return;
        }
        try {
            File optJar = new File(jarFile.getParentFile(), jarFile.getName() + ".opt");
            if (optJar.exists()) {
                optJar.delete();
            }

            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> enumeration = jar.entries();
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar));
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String entryName = jarEntry.getName();
                ZipEntry zipEntry = new ZipEntry(entryName);
                InputStream inputStream = jar.getInputStream(jarEntry);
                jarOutputStream.putNextEntry(zipEntry);

                if (loaderManagerEntryName.equals(entryName)) {
                    jarOutputStream.write(getHackedBytes(inputStream, loaders));
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream));
                }
                inputStream.close();
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
            jar.close();

            if (jarFile.exists()) {
                jarFile.delete();
            }
            optJar.renameTo(jarFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getHackedBytes(InputStream inputStream, List<String> loaders) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new HackClassVisitor(Opcodes.ASM5, cw, loaders);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private static class HackClassVisitor extends ClassVisitor {

        private List<String> loaders = null;

        public HackClassVisitor(int api, ClassVisitor classVisitor, List<String> loaders) {
            super(api, classVisitor);
            this.loaders = loaders;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (name.equals(Constants.LOADER_MANAGER_HACK_METHOD)) {
                mv = new HackMethodVisitor(Opcodes.ASM5, mv, loaders);
            }
            return mv;
        }
    }

    private static class HackMethodVisitor extends MethodVisitor {

        private List<String> loaders = null;

        public HackMethodVisitor(int api, MethodVisitor methodVisitor, List<String> loaders) {
            super(api, methodVisitor);
            this.loaders = loaders;
        }

        @Override
        public void visitInsn(int opcode) {
            for (String loader : loaders) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitTypeInsn(Opcodes.NEW, loader);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, loader, "<init>", "()V", false);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/github/boybeak/irouter/core/LoaderManager", "loadInto", "(Lcom/github/boybeak/irouter/core/BaseLoader;)V", false);
            }

            super.visitInsn(opcode);
        }

    }

}
