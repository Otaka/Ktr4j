package com.ncsdcmp;

import com.ncsdcmp.analyzers.AbstractAnalyzer;
import com.ncsdcmp.analyzers.AnalyzeContext;
import com.ncsdcmp.analyzers.BasicBlockSplitAnalyzer;
import com.ncsdcmp.analyzers.FileParserAnalyzer;
import com.ncsdcmp.analyzers.FunctionSplitterAnalyzer;
import com.ncsdcmp.analyzers.MarkDeadCodeAnalyzer;
import com.ncsdcmp.nwscript.NwScriptParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sad
 */
public class Decompiler {

    public Decompiler(File nwScriptNssFile) throws FileNotFoundException, IOException {
        if (!nwScriptNssFile.exists()) {
            throw new IllegalArgumentException("Cannot find nwscript.nss provided as argument [" + nwScriptNssFile.getAbsolutePath() + "]");
        }

        NwScriptParser nwScriptParser = new NwScriptParser();
        nwScriptParser.parseNWScript(new FileInputStream(nwScriptNssFile));
    }

    private List<AbstractAnalyzer> createAnalyzers() {
        List<AbstractAnalyzer> analyzers = new ArrayList<>();
        analyzers.add(new FileParserAnalyzer());
        analyzers.add(new FunctionSplitterAnalyzer());
        analyzers.add(new BasicBlockSplitAnalyzer());
        analyzers.add(new MarkDeadCodeAnalyzer());
        return analyzers;
    }

    public String decompile(InputStream is) {
        AnalyzeContext context = new AnalyzeContext();
        context.setInputStream(is);

        System.out.println("Start analysis");

        List<AbstractAnalyzer> analyzers = createAnalyzers();
        for (AbstractAnalyzer analyzer : analyzers) {
            System.out.println("\tStart " + analyzer.getClass().getSimpleName());
            analyzer.setContext(context);
            analyzer.analyze();
        }

        return "";
    }
}
