// semi-complex sniper
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


@Mod(modid = HypixelAHSniperBot.MODID, name = HypixelAHSniperBot.NAME, version = HypixelAHSniperBot.VERSION)
public class HypixelAHSniperBot
{
    public static final String MODID = "hypixelahsnipingbot";
    public static final String NAME = "HypixelAH Sniping Bot";
    public static final String VERSION = "1.8.9";

    HashMap<String, Double> averagePrices;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        // load the prices from the .txt file
        averagePrices = new HashMap<>();
        try {
            File pricesFile = new File(event.getModConfigurationDirectory().getParent(), "AvgPrices.txt");
            FileReader reader = new FileReader(pricesFile);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] priceData = line.split(",");
                averagePrices.put(priceData[0], Double.parseDouble(priceData[1]));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // listen for auction house messages
        HypixelAHSniperBot.Listener listener = new HypixelAHSniperBot.Listener();
        HypixelAHSniperBot.NetworkHandler.registerListener(listener);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // register the command
        HypixelAHSniperBot.Command command = new HypixelAHSniperBot.Command();
        HypixelAHSniperBot.NetworkHandler.registerCommand(command);
    }

    public class Listener {
        // listen for auction messages and check if the price is lower than the average price
        public void onMessageReceived(String message){
            if(message.startsWith("Auction") && !message.endsWith("end")){
                String[] messageData = message.split(", ");
                String itemName = messageData[1];
                double price = Double.parseDouble(messageData[2]);
                if(averagePrices.containsKey(itemName) && price < averagePrices.get(itemName)){
                    // snipe the item
                    HypixelAHSniperBot.NetworkHandler.sendMessage("/ah snipe " + itemName);
                }
            }
        }
    }

    public class Command {
        // command to check the average price of an item
        public void onCommandReceived(String command, ArrayList<String> args){
            if(command.equals("/checkprice")){
                String itemName = args.get(0);
                if(averagePrices.containsKey(itemName)){
                    HypixelAHSniperBot.NetworkHandler.sendMessage("The average price of " + itemName + " is " + averagePrices.get(itemName) + " coins.");
                }
            }
        }
    }
}