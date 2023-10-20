/**
 * 
 */
package mod.gottsch.neoforge.ddenizens;

import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.setup.ClientSetup;
import mod.gottsch.neoforge.ddenizens.setup.CommonSetup;
import mod.gottsch.neoforge.ddenizens.setup.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author Mark Gottschling on Apr 5, 2022
 *
 */
@Mod(DD.MODID)
public class DD {
	public static final Logger LOGGER = LogManager.getLogger(DD.MODID);

	public static final String MODID = "ddenizens";

	public DD() {
        // register the deferred registries
        Registration.init();
        Config.register();

        // register the setup method for mod loading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // register 'ModSetup::init' to be called at mod setup time (server and client)
        modEventBus.addListener(CommonSetup::init);
        // register 'ClientSetup::init' to be called at mod setup time (client only)
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init));
    }
}
