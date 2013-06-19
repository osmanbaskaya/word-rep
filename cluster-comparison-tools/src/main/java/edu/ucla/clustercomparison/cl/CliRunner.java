/*
 * Copyright 2013 David Jurgens
 *
 * This file is part of the Cluster-Comparison package and is covered under the
 * terms and conditions therein.
 *
 * The Cluster-Comparison package is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation and distributed hereunder to
 * you.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND NO REPRESENTATIONS OR WARRANTIES,
 * EXPRESS OR IMPLIED ARE MADE.  BY WAY OF EXAMPLE, BUT NOT LIMITATION, WE MAKE
 * NO REPRESENTATIONS OR WARRANTIES OF MERCHANT- ABILITY OR FITNESS FOR ANY
 * PARTICULAR PURPOSE OR THAT THE USE OF THE LICENSED SOFTWARE OR DOCUMENTATION
 * WILL NOT INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER
 * RIGHTS.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.ucla.clustercomparison.cl;

import edu.ucla.clustercomparison.BaseScorer;
import edu.ucla.clustercomparison.Evaluation;

import java.io.*;

import java.util.*;

import edu.ucla.sspace.common.ArgOptions;


/**
 * The base class for providing common command-line functionality.
 */
public abstract class CliRunner {

    public CliRunner() { }   
    
    private ArgOptions getBaseOptions() {
        ArgOptions opts = new ArgOptions();
        opts.addOption('n', "no-remapping",
                          "Treats the input key as having the same sense " +
                          "inventory and does not remap the sense labels",
                          false, null, "Program Options");
        opts.addOption('r', "output-remapped-key",
                          "If the input labeling is remapped, write the new " +
                          "key to the following file",
                          true, "FILE", "Program Options");
        return opts;
    }

    /**
     * Adds any subclass-specific command line options to {@code opts}.
     * Subclasses should override this method if they need to add additional
     * options.
     */
    protected void addMethodSpecificOptions(ArgOptions opts) { }

    /**
     * Returns the evaluation to be used
     */
    abstract Evaluation getEvaluation();

    /**
     * Returns the name for this evaluation to be used in the command line
     * description
     */
    protected abstract String getEvalName();

    public void run(String[] args) {
        ArgOptions opts = getBaseOptions();
        addMethodSpecificOptions(opts);

        opts.parseOptions(args);

        if (opts.numPositionalArgs() != 2) {
            System.out.println(
                "usage: java "  + getEvalName() 
                + " [options] gold-standard.key to-test.key\n"
                + opts.prettyPrint()
                + "\n\n" + 
                "Methods that generate sense labels in the WordNet 3.1 " +
                "sense inventory should\n" + 
                "be sure to use the --no-remapping option to ensure that " +
                "their labels are\n" + 
                "directly compared with the gold standard labels.\n");
            return;
        }
        
        BaseScorer scorer = new BaseScorer() {
                @Override protected Evaluation getEvaluation() {
                    return CliRunner.this.getEvaluation();
                }
            };

        boolean performRemapping = !opts.hasOption("no-remapping");
        File remappedKeyFile = opts.hasOption("output-remapped-key")
            ? new File(opts.getStringOption("output-remapped-key"))
            : null;

        try {
            scorer.score(new File(opts.getPositionalArg(0)),
                         new File(opts.getPositionalArg(1)),
                         remappedKeyFile,
                         performRemapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
