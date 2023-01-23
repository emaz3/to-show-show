// java ah sniper bot thing
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
 @Mod(modid = "AHBot", name="AH Bot", version="1.0")
public class AHBot {
     @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // register event handler
        MinecraftForge.EVENT_BUS.register(this);
    }
     @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
     @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
     @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event)
    {
        // check if message has item price
        if (event.message.contains("Price: "))
        {
            String[] messageSplit = event.message.split(" ");
            int price = Integer.parseInt(messageSplit[1]);
            // check if price is good enuf
            if (price < 100)
            {
                // send command to buy
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/ah find " + messageSplit[0] + " buy 1");
            }
        }
    }
}