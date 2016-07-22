package co.insou.headfix;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class HeadAgent implements ClassFileTransformer {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[HeadFix] Adding transformer!");
        instrumentation.addTransformer(new HeadAgent());
        System.out.println("[HeadFix] has been enabled!");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingTransformed, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className != null) {
                className = className.replace("/", ".");
                if (className.startsWith("net.minecraft.server.") && className.endsWith(".ItemSkull")) {
                    System.out.println("[HeadFix] Correct transformation found!");
                    String version = className.split("\\.")[3];
                    System.out.println("[HeadFix] Found version " + version);
                    ClassPool pool = ClassPool.getDefault();
                    pool.appendClassPath(new ByteArrayClassPath(className, classfileBuffer));

                    CtClass ctClass = pool.get(className);
                    CtMethod method = ctClass.getMethod("a", "(Lnet/minecraft/server/%ver%/NBTTagCompound;)Z".replace("%ver%", version));

                    method.setBody(("{\n" +
                            "        net.minecraft.server.%ver%.NBTTagCompound nbt = $1;\n" +
                            "        super.a(nbt);\n" +
                            "        if (nbt.hasKeyOfType(\"SkullOwner\", 8) && nbt.getString(\"SkullOwner\").length() > 0) {\n" +
                            "            final com.mojang.authlib.GameProfile gameprofile = new com.mojang.authlib.GameProfile(null, nbt.getString(\"SkullOwner\"));\n" +
                            "            net.minecraft.server.%ver%.TileEntitySkull.b(gameprofile, new co.insou.headfix.ProfilePredicate(nbt, \"%ver%\"));\n" +
                            "            return true;\n" +
                            "        }\n" +
                            "        return false;\n" +
                            "    }\n").replace("%ver%", version));

                    System.out.println("[HeadFix] Successfully transformed ItemSkull class!");

                    return ctClass.toBytecode();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}