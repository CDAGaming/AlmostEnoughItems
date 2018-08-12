package com.gmail.zendarva.aie.gui;

import com.gmail.zendarva.aie.api.IDisplayCategory;
import com.gmail.zendarva.aie.api.IRecipe;
import com.gmail.zendarva.aie.gui.widget.AEISlot;
import com.gmail.zendarva.aie.gui.widget.Button;
import com.gmail.zendarva.aie.gui.widget.Control;
import com.gmail.zendarva.aie.plugin.crafting.VanillaCraftingRecipe;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecipeGui extends GuiContainer {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("almostenoughitems","textures/gui/recipecontainer.png");
    private final MainWindow mainWindow;
    private final Container container;
    private final GuiScreen prevScreen;
    private final Map<IDisplayCategory, List<IRecipe>> recipes;
    private int guiWidth = 176;
    private int guiHeight=222;
    ArrayList<IDisplayCategory> categories = new ArrayList<>();
    private int categoryPointer = 0;
    private int recipePointer = 0;
    private List<AEISlot> slots;
    private int cycleCounter =0;
    private int[] itemPointer;
    List<Control> controls = new LinkedList<>();

    public RecipeGui(Container p_i1072_1_, GuiScreen prevScreen, Map<IDisplayCategory, List<IRecipe>> recipes) {
        super(new RecipeContainer());
        this.container = p_i1072_1_;
        this.prevScreen = prevScreen;
        this.recipes = recipes;
        this.mc = Minecraft.getMinecraft();
        this.itemRender=mc.getRenderItem();
        this.fontRenderer= mc.fontRenderer;
        this.mainWindow=Minecraft.getMinecraft().mainWindow;

        setupCategories();
    }

    private void setupCategories() {
        categories.addAll(recipes.keySet());
        updateRecipe();
    }


    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        int y = (int) ((mainWindow.getScaledHeight()/2 - this.guiHeight/2));
        drawCenteredString(this.fontRenderer,categories.get(categoryPointer).getDisplayName(),guiLeft + guiWidth/2,y+9,0x999999);
        controls.forEach(Control::draw);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        cycleCounter++;
        if (cycleCounter > 60) {
            cycleCounter = 0;
            updateItemPointer();
            updateSlots();
        }
    }



    @Override
    public void onResize(Minecraft p_onResize_1_, int p_onResize_2_, int p_onResize_3_) {
        super.onResize(p_onResize_1_, p_onResize_2_, p_onResize_3_);
        updateRecipe();
    }

    private void updateRecipe(){
        IRecipe recipe = recipes.get(categories.get(categoryPointer)).get(recipePointer);
        categories.get(categoryPointer).setRecipe(recipe);
        slots = categories.get(categoryPointer).setupDisplay();

        guiLeft  = (int) ((mainWindow.getScaledWidth()/2 -this.guiWidth/2));
        guiTop  = (int) ((mainWindow.getScaledHeight()/2 - this.guiHeight/2));

        for (AEISlot slot : slots) {
            slot.move(guiLeft,guiTop);
        }

        Button btnCatagoryLeft = new Button(guiLeft+10,guiTop+2,15,20,"<");
        Button btnCatagoryRight = new Button(guiLeft +guiWidth-25,guiTop+2,15,20,">");
        btnCatagoryRight.onClick= this::btnCategoryRight;
        btnCatagoryLeft.onClick= this::btnCategoryLeft;

        Button btnRecipeLeft = new Button(guiLeft+10,guiTop+guiHeight-30,15,20,"<");
        Button btnRecipeRight = new Button(guiLeft +guiWidth-25,guiTop+guiHeight-30,15,20,">");
        btnRecipeRight.onClick= this::btnRecipeRight;
        btnRecipeLeft.onClick= this::btnRecipeLeft;

        controls.clear();
        controls.add(btnCatagoryLeft);
        controls.add(btnCatagoryRight);
        if (categories.size() == 1){
            btnCatagoryLeft.setEnabled(false);
            btnCatagoryRight.setEnabled(false);
        }

        controls.add(btnRecipeLeft);
        controls.add(btnRecipeRight);

        itemPointer = new int[9];
        for (int i = 0; i < itemPointer.length; i++) {
            itemPointer[i] = 0;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

        int lvt_4_1_ = (int) ((mainWindow.getScaledWidth()/2 -this.guiWidth/2));
        int lvt_5_1_ = (int) ((mainWindow.getScaledHeight()/2 - this.guiHeight/2));

        this.drawTexturedModalRect(lvt_4_1_, lvt_5_1_, 0, 0, this.guiWidth, this.guiHeight);
        slots.forEach(AEISlot::draw);
    }


    @Override
    protected void initGui() {
        super.initGui();
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (!super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)){


        if (p_keyPressed_1_ == 259 && prevScreen !=null){
            Minecraft.getMinecraft().displayGuiScreen(prevScreen);
            return true;
        }}
        return false;

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    private boolean btnCategoryLeft(int button){
        categoryPointer--;
        if (categoryPointer < 0){
            categoryPointer= categories.size()-1;

        }
        updateRecipe();
        return true;
    }

    private boolean btnCategoryRight(int button){
        categoryPointer++;
        if (categoryPointer >= categories.size()){
            categoryPointer= 0;
        }
        updateRecipe();
        return true;
    }

    private boolean btnRecipeLeft(int button){
        recipePointer--;
        if (recipePointer < 0){
            recipePointer= recipes.get(categories.get(categoryPointer)).size()-1;
        }
        updateRecipe();
        return true;
    }

    private boolean btnRecipeRight(int button){
        recipePointer++;
        if (recipePointer >= recipes.get(categories.get(categoryPointer)).size()){
            recipePointer= 0;
        }
        updateRecipe();
        return true;
    }

    private void updateItemPointer(){
        IRecipe recipe = recipes.get(categories.get(categoryPointer)).get(recipePointer);
        for (int i = 0; i < recipe.getInput().size(); i++) {
            int convertPointer = getSlotWithSize((VanillaCraftingRecipe) recipe,i);

            if (convertPointer >= recipe.getInput().size())
                    continue;
            List targList= (List) recipe.getInput().get(i);
            if (itemPointer[convertPointer] >= targList.size()-1)
                itemPointer[convertPointer] =0;
            else
                itemPointer[convertPointer]++;
        }
    }

    private void updateSlots() {
        IRecipe recipe = recipes.get(categories.get(categoryPointer)).get(recipePointer);
        for (int i = 0; i < recipe.getInput().size(); i++) {
            int convertPointer = getSlotWithSize((VanillaCraftingRecipe) recipe,i);
            if (convertPointer >= recipe.getInput().size())
                continue;
            List<ItemStack> targList= (List) recipe.getInput().get(i);
            if (targList.isEmpty())
                    continue;
            slots.get(convertPointer).setStack(targList.get(itemPointer[i]));

        }
    }


    private int getSlotWithSize(VanillaCraftingRecipe recipe, int num){
        if (recipe.getWidth() == 1){
            if (num == 1)
                return 3;
            if (num == 2)
                return  6;
        }

        if (recipe.getWidth() == 2){
            if (num == 2)
                return 3;
            if (num == 3)
                return 4;
            if (num == 4)
                return 6;
            if (num ==5)
                return 7;

        }
        return num;
    }
}
