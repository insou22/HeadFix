# HeadFix
HeadFix is a resource designed to remove the new skull signature verification introduced into the Minecraft server in version 1.10+
This uses Java Agents to change the NBT packaging method's bytecode and remove the new verification.
This means skull plugins such as [this one by me](https://www.spigotmc.org/resources/heads.26467/) and [this one by sothatsit](https://www.spigotmc.org/resources/heads-1500-heads-add-your-own.13402/) will still work in versions 1.10+

# Installation
Either manually compile the project or download the jar from the [releases](https://github.com/insou22/HeadFix/releases).
Paste the downloaded jar in your server's startup directory (the one that contains your spigot jar, your startup script, etc.)
Next open your server's startup script in your favourite editor, it should look something like this:

    java -jar spigot-1.10.2.jar
    
Edit this to include the javaagent parameter, it should look like this:

    java -javaagent:"HeadFix-v1.0-SNAPSHOT.jar -jar spigot-1.10.2.jar"

Start the server and... that's it! You're done, the signature verification will be removed at runtime and your favourite head plugin will begin working again!
