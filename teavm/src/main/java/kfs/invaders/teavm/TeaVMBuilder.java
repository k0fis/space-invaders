package kfs.invaders.teavm;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import java.io.File;
import java.io.IOException;
import org.teavm.backend.wasm.WasmDebugInfoLevel;
import org.teavm.tooling.TeaVMSourceFilePolicy;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.tooling.sources.DirectorySourceFileProvider;
import org.teavm.vm.TeaVMOptimizationLevel;

public class TeaVMBuilder {
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
        teaBuildConfiguration.assetsPath.add(new AssetFileHandle("../assets"));
        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();
        teaBuildConfiguration.htmlTitle = "KFS Space Invaders pro Kubu";

        TeaBuilder.config(teaBuildConfiguration);
        TeaVMTool tool = new TeaVMTool();

        tool.setTargetType(TeaVMTargetType.JAVASCRIPT);
        tool.setMainClass(TeaVMLauncher.class.getName());
        tool.setOptimizationLevel(DEBUG ? TeaVMOptimizationLevel.SIMPLE : TeaVMOptimizationLevel.ADVANCED);
        tool.setObfuscated(!DEBUG);

        if(DEBUG) {
            tool.setDebugInformationGenerated(true);
            tool.setSourceMapsFileGenerated(true);
            tool.setWasmDebugInfoLevel(WasmDebugInfoLevel.FULL);
            tool.setSourceFilePolicy(TeaVMSourceFilePolicy.COPY);
            tool.addSourceFileProvider(new DirectorySourceFileProvider(new File("../core/src/main/java/")));
        }

        TeaBuilder.build(tool);
    }
}
