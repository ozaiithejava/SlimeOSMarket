package net.ozaii.slimeOSCentry.managers.market.obj;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Product {

    private String name;
    private ItemStack icon;
    private int amount;
    private List<String> description;
    private String category;
    private int guislot;
    private List<String> command;

    public Product(String name, ItemStack icon, int amount, List<String> description,String category,int guislot,List<String> command) {
        this.name = name;
        this.icon = icon;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.guislot = guislot;
        this.command = command;
    }
    /* getter */
    public String getName() {return name;}
    public ItemStack getIcon() {return icon;}
    public int getAmount() {return amount;}
    public List<String> getDescription() {return description;}
    public String getCategory() {return category;}
    public int getGuislot() {return guislot;}
    public List<String> getCommand() {return command;}

    /* setters */
    public void setName(String name) {this.name = name;}
    public void setIcon(ItemStack icon) {this.icon = icon;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setDescription(List<String> description) {this.description = description;}
    public void setCategory(String category) {this.category = category;}
    public void setGuislot(int guislot) {this.guislot = guislot;}
    public void setCommand(List<String> command) {this.command = command;}

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", icon=" + icon.getType() + // Displaying the material of the icon ItemStack
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", guislot=" + guislot +
                ", command='" + command + '\'' +
                '}';
    }

}
