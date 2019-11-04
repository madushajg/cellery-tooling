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

import * as path from "path";
import * as vscode from "vscode";
import Constants from "./constants";

/**
 * Cellery commands.
 */
class Commands {
    private static readonly terminals: { [name: string]: vscode.Terminal } = { };

    /**
     * Register Cellery Commands
     */
    public static registerCelleryCommands = (context: vscode.ExtensionContext) => {
        context.subscriptions.push(vscode.commands.registerCommand(Constants.commands.CELLERY_BUILD,
                                                                   Commands.handleBuildCommand));
        context.subscriptions.push(vscode.commands.registerCommand(Constants.commands.CELLERY_RUN,
                                                                   Commands.handleRunCommand));
    }

    /**
     * cellery build command handler used by the function 'registerCommand'
     */
    private static readonly handleBuildCommand = async() => {
        const cellName = await vscode.window.showInputBox({
            placeHolder: `${Constants.ORG_NAME}/${Constants.IMAGE_NAME}:${Constants.VERSION}`,
            prompt: `Enter the cell name`,
        });
        const file = vscode.window.activeTextEditor ? vscode.window.activeTextEditor.document.fileName : null;
        const cwd = vscode.workspace.workspaceFolders ? vscode.workspace.workspaceFolders[0].uri : null;
        if (cellName && file && cwd) {
            const buildCommand = `${Constants.CELLERY_BUILD_COMMAND} ${file} ${cellName}`;
            const assocTerminal = Commands.terminals.build;
            if (Commands.terminals.build) {
                delete Commands.terminals.build;
                assocTerminal.dispose();
            }
            const terminal = vscode.window.createTerminal({ name: "Cellery Build", cwd: cwd});
            Commands.terminals.build = terminal;
            terminal.show(true);
            terminal.sendText(buildCommand);
            return;
        }
        vscode.window.showErrorMessage("Something went wrong while running cellery build");
    }

    /**
     * cellery run command handler used by the function 'registerCommand'
     */
    private static readonly handleRunCommand = async() => {
        const cellName = await vscode.window.showInputBox({
            placeHolder: `${Constants.ORG_NAME}/${Constants.IMAGE_NAME}:${Constants.VERSION}`,
            prompt: `Enter the cell name`,
        });
        const instanceName = await vscode.window.showInputBox({
            value: `${vscode.window.activeTextEditor ? path.parse(vscode.window.activeTextEditor.document.fileName).name : "my-instance"}`,
            prompt: `Enter the cell instance name`,
        });
        const file = vscode.window.activeTextEditor ? vscode.window.activeTextEditor.document.fileName : null;
        const cwd = vscode.workspace.workspaceFolders ? vscode.workspace.workspaceFolders[0].uri : null;
        if (cellName && instanceName && file && cwd) {
            const buildCommand = `${Constants.CELLERY_BUILD_COMMAND} ${file} ${cellName}`;
            const runCommand = `${Constants.CELLERY_RUN_COMMAND} ${cellName} -n ${instanceName} -d`;
            const logCommand = `${Constants.CELLERY_LOGS_COMMAND} ${instanceName}`;
            const assocTerminal = Commands.terminals.run;
            if (Commands.terminals.run) {
                delete Commands.terminals.run;
                assocTerminal.dispose();
            }
            const terminal = vscode.window.createTerminal({ name: "Cellery Ruild", cwd: cwd});
            Commands.terminals.run = terminal;
            terminal.show(true);
            terminal.sendText(`${buildCommand} && ${runCommand} && ${logCommand}`);
            return;
        }
        vscode.window.showErrorMessage("Something went wrong while running cellery run");
    }
}

export default Commands;
