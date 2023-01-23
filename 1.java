// simple ah sniper thing
import java.io.*; 
import java.util.*; 
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.util.text.TextComponentString;
 public class HypixelAHSnipeBot {
     private static Minecraft mc = Minecraft.getMinecraft();
    private static NetHandlerPlayClient connection = mc.getConnection();
    private static GameSettings settings = mc.gameSettings;
     public static void main(String[] args) throws IOException {
         // reads the text file containing the average prices of items
        File priceFile = new File("price_list.txt");
        Scanner priceScanner = new Scanner(priceFile);
         while (priceScanner.hasNextLine()) {
            String line = priceScanner.nextLine();
            String[] itemPrice = line.split(",");
            String itemName = itemPrice[0];
            double averagePrice = Double.parseDouble(itemPrice[1]);
             // checks if the item is listed on the ah and at a lower price than the average
            if (checkAHForItem(itemName, averagePrice)) {
                buyItem(itemName);
            }
        }
    }
     // checks the ah for the given item and returns true if it is found at a lower price than the average
    public static boolean checkAHForItem(String itemName, double averagePrice) {
        // searches for the item on the ah
        connection.sendPacket(new CPacketChatMessage("/ah search " + itemName));
        // gets the first result from the search
        String firstResult = mc.ingameGUI.getChatGUI().getChatLines().get(mc.ingameGUI.getChatGUI().getChatLines().size() - 1).getUnformattedText();
        // gets the price from the result
        String priceString = firstResult.split(" ")[2];
        double price = Double.parseDouble(priceString);
        // returns true if the price is lower than the average
        return price < averagePrice;
    }
     // buy item from ah
    public static void buyItem(String itemName) {
        // opens ah
        connection.sendPacket(new CPacketChatMessage("/ah"));
        // search for item
        connection.sendPacket(new CPacketChatMessage("/ah search " + itemName));
        // clicks the item
        connection.sendPacket(new CPacketClickWindow(0, 0, 0, ClickType.QUICK_MOVE, new ItemStack(Blocks.AIR), (short)0));
        // buys the item
        connection.sendPacket(new CPacketConfirmTransaction(0, 0, true));
        // closes the ah
        connection.sendPacket(new CPacketCloseWindow(0));
    }
}