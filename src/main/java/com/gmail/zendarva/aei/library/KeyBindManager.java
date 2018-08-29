package com.gmail.zendarva.aei.library;

import com.gmail.zendarva.aei.listenerdefinitions.PreLoadOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.dimdev.rift.listener.client.KeybindHandler;

import java.util.*;

/**
 * Created by James on 8/7/2018.
 */
public class KeyBindManager implements PreLoadOptions, KeybindHandler {

    private static boolean optionsLoaded =false;
    private static List<KeyBinding> bindingsToAdd = new ArrayList<>();
    private static Map<KeyBinding, Sink> bindingFunctions = new HashMap<>();

    public static KeyBinding createKeybinding(String bindingName, int key, String categoryName, Sink function){
        KeyBinding newBinding;
        newBinding = new KeyBinding(bindingName, key, categoryName);
        if (optionsLoaded) {
            ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, newBinding);
        }
        else
        {
            bindingsToAdd.add(newBinding);
        }
        bindingFunctions.put(newBinding,function);
        addCategoryIfMissing(categoryName);
        return newBinding;
    }

    private static void addCategoryIfMissing(String categoryName) {
        if (!KeyBinding.CATEGORY_ORDER.containsKey(categoryName)){
            KeyBinding.CATEGORY_ORDER.put(categoryName,KeyBinding.CATEGORY_ORDER.size()+1);
        }
    }

    @Override
    public List<KeyBinding> loadOptions() {
        optionsLoaded=true;
        return bindingsToAdd;
    }

    @Override
    public void processKeybinds() {
        bindingFunctions.keySet().stream().filter(KeyBinding::isPressed).forEach(f->bindingFunctions.get(f).Sink());
    }

    public static boolean processGuiKeybinds(int typedChar){
        Optional<KeyBinding> binding= bindingFunctions.keySet().stream().filter(f->f.keyCode.getKeyCode() == typedChar).findFirst();
        if (binding.isPresent()){
            bindingFunctions.get(binding.get()).Sink();
            return true;
        }
        return false;
    }
}
