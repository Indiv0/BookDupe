/**
 *
 * @author Indivisible0
 */
package com.github.Indiv0.util;

import net.minecraft.server.NBTTagCompound;

public class NBTTagUtil extends Util {
    public static void transferNBTTagList(NBTTagCompound sourceTag, NBTTagCompound targetTag, String list) {
        // Checks to make sure that the enchantments exist.
        if (sourceTag.getList(list).size() == 0)
            return;

        // Transfers any enchantments to the new tag.
        targetTag.set(list, sourceTag.getList(list));
    }
}
