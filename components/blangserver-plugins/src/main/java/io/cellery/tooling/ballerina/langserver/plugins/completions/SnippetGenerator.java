/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.cellery.tooling.ballerina.langserver.plugins.completions;

import io.cellery.tooling.ballerina.langserver.plugins.Constants;
import io.cellery.tooling.ballerina.langserver.plugins.visitor.Component;
import org.ballerinalang.langserver.SnippetBlock;
import org.ballerinalang.langserver.completions.util.ItemResolverConstants;

import java.util.Map;

/**
 * Cellery Snippet Generator.
 */
public class SnippetGenerator {

    /**
     * Get Cellery Component Snippet Block.
     *
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getComponentSnippet() {
        return new SnippetBlock(Constants.CompletionProvider.COMPONENT_SNIPPET_LABEL,
                "cellery:Component ${3:component} = {\n" +
                            "\tname: \"${1:componentName}\",\n" +
                            "\tsrc: {\n" +
                                "\t\timage: \"${2:image}\"\n" +
                            "\t}\n" +
                        "};",
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    /**
     * Get Cellery Cell Image Snippet Block.
     *
     * @param components Components that should be part of the Cell
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getCellImageSnippet(Map<String, Component> components) {
        String componentsString = generateComponentsMapEntries(components, "\t");
        return new SnippetBlock(Constants.CompletionProvider.CELL_SNIPPET_LABEL,
                "cellery:CellImage ${1:cell} = {\n" +
                            "\tcomponents: " + componentsString + "\n" +
                        "};",
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    /**
     * Get Cellery Composite Image Snippet Block.
     *
     * @param components Components that should be part of the Composite
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getCompositeImageSnippet(Map<String, Component> components) {
        String componentsString = generateComponentsMapEntries(components, "\t");
        return new SnippetBlock(Constants.CompletionProvider.COMPOSITE_SNIPPET_LABEL,
                "cellery:Composite ${1:composite} = {\n" +
                            "\tcomponents: " + componentsString + "\n" +
                        "};",
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    /**
     * Generate the entries in the components map.
     *
     * @param components Components that should be part of the map
     * @param leadingPadding Padding to be added before the map block
     * @return The components map string
     */
    private static String generateComponentsMapEntries(Map<String, Component> components, String leadingPadding) {
        StringBuilder componentsStringBuilder = new StringBuilder();
        boolean isFirstEntry = true;
        for (Map.Entry<String, Component> componentsEntry: components.entrySet()) {
            if (isFirstEntry) {
                isFirstEntry = false;
            } else {
                componentsStringBuilder.append(",");
            }
            componentsStringBuilder.append("\n")
                    .append(leadingPadding)
                    .append("\t")
                    .append(componentsEntry.getValue().getName())
                    .append(": ")
                    .append(componentsEntry.getKey());
        }
        String componentsString = componentsStringBuilder.toString();
        if (!"".equals(componentsString)) {
            componentsString = componentsString + "\n" + leadingPadding;
        }
        return "{" + componentsString + "}";
    }

    /**
     * Get Cell Build function snippet.
     *
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getCellBuildFunctionSnippet() {
        String snippet = "public function build(cellery:ImageName iName) returns error? {"
                + System.lineSeparator() + "\tcellery:Component ${3:component} = {"
                + System.lineSeparator() + "\t\tname: \"${1:component-name}\","
                + System.lineSeparator() + "\t\tsrc: {"
                + System.lineSeparator() + "\t\t\timage: \"${2:image}\"\n"
                + System.lineSeparator() + "\t\t}"
                + System.lineSeparator() + "\t};"
                + System.lineSeparator() + "\tcellery:CellImage ${4:cell} = {"
                + System.lineSeparator() + "\t\tcomponents: {"
                + System.lineSeparator() + "\t\t\"${1:component-name}\": ${3:component}"
                + System.lineSeparator() + "\t\t}"
                + System.lineSeparator() + "\t};"
                + System.lineSeparator() + "\treturn <@untainted> cellery:createImage(${4:cell}, iName);"
                + System.lineSeparator() + "}";
        return new SnippetBlock(Constants.CompletionProvider.CELL_BUILD_FUNCTION_LABE, snippet,
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    /**
     * Get Composite Build function snippet.
     *
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getCompositeBuildFunctionSnippet() {
        String snippet = "public function build(cellery:ImageName iName) returns error? {"
                + System.lineSeparator() + "\tcellery:Component ${3:component} = {"
                + System.lineSeparator() + "\t\tname: \"${1:component-name}\","
                + System.lineSeparator() + "\t\tsrc: {"
                + System.lineSeparator() + "\t\t\timage: \"${2:image}\"\n"
                + System.lineSeparator() + "\t\t}"
                + System.lineSeparator() + "\t};"
                + System.lineSeparator() + "\tcellery:Composite ${4:composite} = {"
                + System.lineSeparator() + "\t\tcomponents: {"
                + System.lineSeparator() + "\t\t\t\"${1:component-name}\": ${3:component}"
                + System.lineSeparator() + "\t\t}"
                + System.lineSeparator() + "\t};"
                + System.lineSeparator() + "\treturn <@untainted> cellery:createImage(${4:composite}, iName);"
                + System.lineSeparator() + "}";
        return new SnippetBlock(Constants.CompletionProvider.COMPOSITE_BUILD_FUNCTION_LABEL, snippet,
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    /**
     * Get Cell/Composite Run function snippet.
     *
     * @return {@link SnippetBlock} Generated Snippet Block
     */
    public static SnippetBlock getRunFunctionSnippet() {
        String snippet = "public function run(cellery:ImageName iName, map<cellery:ImageName> instances, "
                + "boolean startDependencies, boolean shareDependencies) returns (cellery:InstanceState[]|error?) {"
                + System.lineSeparator() + "\tcellery:CellImage|cellery:Composite ${1:image} = "
                + "cellery:constructImage(iName);"
                + System.lineSeparator() + "\treturn <@untainted> cellery:createInstance(${1:image}, iName, "
                + "instances, startDependencies, shareDependencies);"
                + System.lineSeparator() + "}";
        return new SnippetBlock(Constants.CompletionProvider.RUN_FUNCTION_LABEL, snippet,
                ItemResolverConstants.SNIPPET_TYPE,
                SnippetBlock.SnippetType.SNIPPET);
    }

    private SnippetGenerator() {    // Prevent initialization
    }
}
