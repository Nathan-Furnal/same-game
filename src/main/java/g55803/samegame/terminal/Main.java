package g55803.samegame.terminal;

import g55803.samegame.model.Facade;
import g55803.samegame.terminal.controller.TermController;
import g55803.samegame.terminal.termview.TerminalView;

/**
 * @author Nathan Furnal
 */
public class Main {
    public static void main(String[] args) {
        Facade model = new Facade();
        TerminalView view = new TerminalView(model);
        TermController ctrl = new TermController(model, view);
        ctrl.start();
    }
}
