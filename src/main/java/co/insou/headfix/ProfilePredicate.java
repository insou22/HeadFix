package co.insou.headfix;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;

import java.lang.reflect.Method;

public class ProfilePredicate implements Predicate<GameProfile> {

    private final Object nbtTagCompound;
    private final String version;

    public ProfilePredicate(Object nbtTagCompound, String version) {
        this.nbtTagCompound = nbtTagCompound;
        this.version = version;
    }

    @Override
    public boolean apply(GameProfile gameProfile) {
        try {
            Class<?> tagClass = nbtTagCompound.getClass();
            Class<?> serializerClass = Class.forName("net.minecraft.server.%ver%.GameProfileSerializer".replace("%ver%", version));

            Method tagSetMethod = tagClass.getDeclaredMethod("set", String.class, tagClass);
            Method serializeMethod = serializerClass.getDeclaredMethod("serialize", tagClass, GameProfile.class);

            Object serialized = serializeMethod.invoke(null, tagClass.getDeclaredConstructor().newInstance(), gameProfile);

            tagSetMethod.invoke(nbtTagCompound, serialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}