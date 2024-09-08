package io.github.jw.spigot.mc.tiktok.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WizardBookCommand implements CommandExecutor {

    @Data
    public class BookData {
        String identifier;
        Material material;
        String title;
        String author;
        String content;
        List<String> lore;
        String playerMessage;
    }

    Map<String, BookData> bookTypes = new HashMap<>();

    public WizardBookCommand() {
        BookData wizardData = new BookData();
        wizardData.setLore(List.of("Hello", "Word"));
        wizardData.setAuthor("author");
        wizardData.setTitle("title");
        wizardData.setContent("content");
        wizardData.setIdentifier("wizard");
        wizardData.setContent("content");
        wizardData.setMaterial(Material.BOOK);

        BookData mageData = new BookData();
        mageData.setLore(List.of("Hello", "Word"));
        mageData.setAuthor("author");
        mageData.setTitle("title");
        mageData.setContent("content");
        mageData.setIdentifier("mage");
        mageData.setContent("content");
        mageData.setMaterial(Material.DIAMOND);

        //Adding the objects into to HashMap
        bookTypes.put("wizard", mageData);
        bookTypes.put("mage", mageData);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player player)) {
            return false; //exit when not triggered by player
        }

        if (strings.length == 0) {
            return false; //exit when have not parameters
        }
        String bookType = strings[0].toLowerCase();

        if (!bookTypes.containsKey(bookType)) {
            return false;  //exit when book type is not register into the `bookTypes`
        }
        BookData data = bookTypes.get(bookType);

        ItemStack book = new ItemStack(data.getMaterial());
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(data.getTitle());
        bookMeta.setAuthor(data.getAuthor());
        bookMeta.addPage(data.getContent());
        bookMeta.setLore(data.getLore());

        book.setItemMeta(bookMeta);
        player.getInventory().addItem(book);
        player.sendTitle("", data.getPlayerMessage(), 20, 5, 20);
        return true;
    }
}
