package net.minecraft.src;
import java.lang.reflect.Field;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import net.minecraft.client.Minecraft;
import pw.geckonerd.cursedrbloader.CursedInjection;
import pw.geckonerd.cursedrbloader.CursedRBLoader;
import pw.geckonerd.cursedrbloader.ModBase;

public class CursedQoL extends ModBase {
	@Override public String[] getDependencies() { return new String[] {}; }
	@Override public String getModID() { return "cursedqol"; }
	@Override public int getVersion() { return 1; }
	@Override public String getTargetClientVersion() { return "20231029"; }

	@Override
	public void onEarlyEnable() throws Exception {
		log("Loading!");
		CursedRBLoader.inject(GuiIngame.class, new CursedInjection() {
			@Override public void transform(ClassPool pool, CtClass clazz) throws Exception {
				// this is so bad
				pool.importPackage("pw.geckonerd.cursedrbloader");
				CtMethod m = clazz.getDeclaredMethod("a", new CtClass[] {CtClass.floatType, CtClass.booleanType, CtClass.intType, CtClass.intType});
				
        		m.insertAt(69, "	Object cqol = CursedRBLoader.getModByID(\"cursedqol\");"
        					  + "	cqol.getClass().getMethod(\"onGuiIngame\", new Class[] {aB.class}).invoke(cqol, new Object[] {$0});");
        		
        		m = clazz.getDeclaredMethod("a", new CtClass[] {CtClass.intType, CtClass.intType, CtClass.intType, CtClass.floatType});

        		m.insertAfter("	Object cqol = CursedRBLoader.getModByID(\"cursedqol\");"
        					  + "	cqol.getClass().getMethod(\"onRenderInvSlot\", new Class[] {aB.class}).invoke(cqol, new Object[] {$0});");
			}
		});
	}
	
	
	private boolean tickProcessed = false;
	
	public void onGuiIngame(GuiIngame guiIngame) throws Exception {
		tickProcessed = false;
	}
	public void onRenderInvSlot(GuiIngame guiIngame) throws Exception {
		if(!tickProcessed)
		{
			//log("tick!");
			tickProcessed = true;
			
			Field itemRenderer = GuiIngame.class.getDeclaredField("dE");
			itemRenderer.setAccessible(true);
			RenderItem renderer = (RenderItem)itemRenderer.get(null);
			
			Field mc = GuiIngame.class.getDeclaredField("dR");
			mc.setAccessible(true);
			Minecraft minecrft = (Minecraft)mc.get(guiIngame);
			
			//renderer.renderItemIntoGUI(minecrft.fontRenderer, minecrft.renderEngine, minecrft.thePlayer.inventory.mainInventory[0], 200, 200);
			
			for(int index = 0; index < 4; index++)
			{
				// div by 2 is required for some reason
				int w = ((minecrft.displayWidth / 2) + (-(-40 * (index - 2)))) / 2;
				int h = (minecrft.displayHeight / 2) - 64;
				
				//log("Width for index " + index + " is " + w + ", h is " + h + ", IS is " + minecrft.thePlayer.inventory.armorInventory[index]);
				renderer.renderItemIntoGUI(minecrft.fontRenderer,
										   minecrft.renderEngine, 
										   minecrft.thePlayer.inventory.armorInventory[index], 
										   w,  // fuck this so hard smh
										   h);
				renderer.renderItemOverlayIntoGUI(minecrft.fontRenderer,
						   minecrft.renderEngine, 
						   minecrft.thePlayer.inventory.armorInventory[index], 
						   w,  // fuck this so hard smh
						   h);
			}
		}
	}
}