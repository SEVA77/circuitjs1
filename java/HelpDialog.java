/*    
    Copyright (C) Usevalad Khatkevich
*/

package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;

public class HelpDialog extends DialogBox {
	
    	HorizontalPanel hp;
	VerticalPanel vp;
	Button okButton;
	ScrollPanel sp;
	ScrollPanel sp2;
	CirSim sim;
	
	HelpDialog() {
		super();
		vp = new VerticalPanel();
		setWidget(vp);
		setText(sim.LS("Help"));
		HTML contentsEN = new HTML("<h3>Warning! If you work with the program using the keyboard, then after any interaction (except scrolling) with the sidebar, right-click on the empty area of the top panel. The disappearance of the functionality of the keys after interacting with the sidebar is a feature of the JavaScript interpreted by the NW.js shell.</h3>" +
		"<h3>The program works only with the English keyboard layout.</h3><hr>" +
		"<h2>Contents:</h2>\n" + 
		"    <ol type=l>\n" + 
		"        <li>\n" + 
		"            <p>\n" + 
		"                <a href=\"#p1\">How to use this</a>\n" + 
		"            </p>" +
		"<li>\n" + 
		"                <p>\n" + 
		"                    <a href=\"#p2\">Drawing and Editing Circuits</a>\n" + 
		"                </p>\n" + 
		"                <li>\n" + 
		"                    <p>\n" + 
		"                        <a href=\"#p3\">High Frequency Circuits</a>\n" + 
		"                    </p>\n" + 
		"                    <li>\n" + 
		"                        <p>\n" + 
		"                            <a href=\"#p4\">Simulation != Real Life</a>\n" + 
		"                        </p>\n" + 
		"                        <li>\n" + 
		"                            <p>\n" + 
		"                                <a href=\"#p5\">Some Errors</a>\n" + 
		"                            </p>\n" + 
		"                            <li>\n" + 
		"                                <p>\n" + 
		"                                    <a href=\"#p6\">License</a>\n" + 
		"                                </p>\n" + 
		"    </ol>\n" + 
		"    <h4>The main part of the information is taken from the http://lushprojects.com/circuitjs/\n" + 
		"    </h4>\n" + 
		"    <form action=\"#\" method=\"GET\">\n" + 
		"        <fieldset>\n" + 
		"            <legend>\n" + 
		"                <h2 id=\"p1\">How to use this</h2>\n" + 
		"            </legend>\n" + 
		"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">When the simulator starts up you will see an animated schematic of a simple LRC circuit. The green colour indicates\n" + 
		"                positive voltage. The grey colour indicates ground. A red colour indicates negative voltage. The moving yellow\n" + 
		"                dots indicate current.</p>\n" + 
		"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">To turn a switch on or off, just click on it. If you move the mouse over any component of the circuit, you will\n" + 
		"                see a short description of that component and its current state in the lower right corner of the window.\n" + 
		"                To modify a component, move the mouse over it, click the right mouse button (or control-click if you have\n" + 
		"                a Mac) and select &quot;Edit&quot;. You can also access the edit function by double-clicking on a component.</p>\n" + 
		"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">There are three graphs at the bottom of the window; these act like oscilloscopes, each one showing the voltage\n" + 
		"                and current across a particular component. Voltage is shown in green, and current is shown in yellow. The\n" + 
		"                current may not be visible if the voltage graph is on top of it. The peak value of the voltage in the scope\n" + 
		"                window is also shown. Move the mouse over one of the scope views, and the component it is graphing will be\n" + 
		"                highlighted. To modify or remove a scope, click the right mouse button over it and choose &quot;remove&quot;\n" + 
		"                from the menu. There are also many other scope options in this context menu. To view a component in the scope,\n" + 
		"                click the right mouse button over the component and select &quot;View in Scope&quot;.</p>\n" + 
		"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The &quot;Circuits&quot; menu contains a lot of sample circuits for you to try.</p>\n" + 
		"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Some circuits, eg Basics-&gt;Potentiometer, contain potentiometers or variable voltage sources. These can be\n" + 
		"                adjusted using sliders that are added to the right hand tool bar, or by positioning the mouse pointer over\n" + 
		"                the component and using the scroll wheel.</p>\n" + 
		"        </fieldset>\n" + 
		"<br>\n" + 
		"        <form action=\"#\" method=\"GET\">\n" + 
		"            <fieldset>\n" + 
		"                <legend>\n" + 
		"                    <h2 id=\"p2\">Drawing and Editing Circuits</h2>\n" + 
		"                </legend>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">You can get a blank circuit by choosing &quot;Blank Circuit&quot; from the &quot;Circuits&quot; menu. You\n" + 
		"                    will need to add at least one voltage source to start the simulator.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">To add components or a wire choose one of the &quot;Add....&quot; options from the &quot;Draw&quot; menu.\n" + 
		"                    Note that common components have keyboard short-cuts to select their add mode. When in add mode the cursor\n" + 
		"                    changes to a &quot;+&quot;. Click and drag the mouse to add a component.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Components may be moved and resized in the selection mode. When in selection mode the cursor changes to an\n" + 
		"                    arrow. Choose &quot;Select/Drag Sel&quot; from the &quot;Draw&quot; menu or press &quot;space&quot;,\n" + 
		"                    or press &quot;escape&quot; to go in to selection mode. Hovering over a component will highlight it and\n" + 
		"                    show information about that component in the info area. Clicking and dragging on a component will move\n" + 
		"                    the component. If you click and drag on the square handles or hold down the ctrl key this will resize\n" + 
		"                    the component and move the terminals.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Wires only connect at their ends and not in the middle therefore you have to draw each segment of a wire\n" + 
		"                    separately. If the simulator spots unconnected points it thinks you intended to connect it will highlight\n" + 
		"                    these with a red circle.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Many components have adjustments that can be made using the edit function explained above. For resistors,\n" + 
		"                    capacitors and inductors you can conveniently set the value from the E12 range by rolling the mouse wheel\n" + 
		"                    when hovering over the component</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The File menu allows you to import or export circuit description files.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The Reset button resets the circuit to a reasonable state. If the simulation is paused then hitting the Reset\n" + 
		"                    button twice will restart it. The Run/Stop button allows you to pause the simulation. The Simulation\n" + 
		"                    Speed slider allows you to adjust the speed of the simulation. If the simulation isn&#39;t time-dependent\n" + 
		"                    (that is, if there are no capacitors, inductors, or time-dependent voltage sources), then this won&#39;t\n" + 
		"                    have any effect. The Current Speed slider lets you adjust the speed of the dots, in case the currents\n" + 
		"                    are so weak (or strong) that the dots are moving too slowly (or too quickly).</p>\n" + 
		"            </fieldset>\n" + 
		"        </form>\n" + 
		"<br>\n" + 
		"        <form action=\"#\" method=\"GET\">\n" + 
		"            <fieldset>\n" + 
		"                <legend>\n" + 
		"                    <h2 id=p3>High Frequency Circuits</h2>\n" + 
		"                </legend>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">This simulator simulates the circuit using a series of short time steps. In each step the changes to the\n" + 
		"                    voltages and currents in the circuit are calculated based on the component models and the current circuit\n" + 
		"                    state. For this process to work the time steps used need to be significantly shorter than the duration\n" + 
		"                    of any event of interest in the circuit. Or, if you prefer, the time steps need to be significantly shorter\n" + 
		"                    than the period of the highest frequency signal of interest.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">By default the simulator uses a 5&micro;s step size. This is OK for audio frequency signals but not for radio\n" + 
		"                    frequency signals or fast digital signals. The step size can be changed from the &quot;Other Options...&quot;\n" + 
		"                    dialog on the options menu. For comparison, the transmission line example in the application uses a 5ps\n" + 
		"                    step size.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The step size shouldn&#39;t be confused with the &quot;Simulation Speed&quot; controlled by the slider in\n" + 
		"                    the right hand panel. The step size controls how long (in simulated time) each step is. The &quot;Simulation\n" + 
		"                    Speed&quot; slider controls how often (in real time) the computer calculates a step.</p>\n" + 
		"\n" + 
		"            </fieldset>\n" + 
		"        </form>\n" + 
		"<br>\n" + 
		"        <form action=\"#\" method=\"GET\">\n" + 
		"            <fieldset>\n" + 
		"                <legend>\n" + 
		"                    <h2 id=p4>Simulation != Real Life</h2>\n" + 
		"                </legend>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Physics simulations are not real life, and don&#39;t assume that simulation and reality are identical! This\n" + 
		"                    simulation idealizes many components. Wires and component leads have no resistance. Voltage sources are\n" + 
		"                    ideal - they will try and supply infinite current if you let them. Capacitors and inductors are 100%\n" + 
		"                    efficient. Logic gate inputs draw zero current - not too bad as an approximation for CMOS logic, but\n" + 
		"                    not typical of 1980s TTL for example. By all means use this simulator to help visualize circuits, but\n" + 
		"                    always test in reality.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Sorry to break it to you folks, but the simulator numerically approximates models of components that are\n" + 
		"                    also approximate. Even without allowing for any bugs it is just a rough guide to reality. This simulator\n" + 
		"                    may be helpful for visualization, but used the wrong way any simulator can give a false sense of security.\n" + 
		"                    Some people don&#39;t really grasp this important concept - I&#39;ve even had one user accuse the simulator\n" + 
		"                    of &quot;lying&quot; because he (or she) didn&#39;t take account of the component idealizations and didn&#39;t\n" + 
		"                    understand the actual performance of the components they chose to use. It&#39;s a key leaning for all\n" + 
		"                    electronic engineers that they must always be fully aware of real-world component (and system) characteristics\n" + 
		"                    and how these differ from any particular simulator they use. If you want more precise models of real-world\n" + 
		"                    components then the SPICE-based simulators are much more appropriate tools than this one, but even then,\n" + 
		"                    you should be aware of deviations from reality. As the great analogue circuit designer Bob Pease said\n" + 
		"                    &quot;When a computer tries to simulate an analog circuit, sometimes it does a good job; but when it\n" + 
		"                    doesn&#39;t, things get very sticky&quot;.</p>\n" + 
		"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">One consequence of the use of ideal components is that the simulator doesn&#39;t converge on a result for\n" + 
		"                    circuits that have no defined behaviour - for example an ideal voltage source short-circuited by an ideal\n" + 
		"                    wire. Another situation that can&#39;t be simulated under these assumptions is the current distribution\n" + 
		"                    between the conductors if two perfect conductors are connected in parallel. When using the simulator\n" + 
		"                    you must account for places where real electronics differs from the ideal.</p>\n" + 
		"            </fieldset>\n" + 
		"        </form>\n" + 
		"<br>\n" + 
		"        <form action=\"#\" method=\"GET\">\n" + 
		"            <fieldset>\n" + 
		"                <legend>\n" + 
		"                    <h2 id=\"p5\">Some Errors</h2>\n" + 
		"                </legend>\n" + 
		"                <p style=\"font-family: Arial, Helvetica, sans-serif;\">Here are some errors you might encounter:</p>\n" + 
		"                <ul style=\"margin-top: 0px; margin-bottom: 0px; font-family: Arial, Helvetica, sans-serif;\">\n" + 
		"                    <li style=\"text-align: justify;\">Voltage source loop with no resistance! - this means one of the voltage sources in your circuit is shorted.\n" + 
		"                        Make sure there is some resistance across every voltage source.</li>\n" + 
		"                    <li style=\"text-align: justify;\">Capacitor loop with no resistance! - it&#39;s not allowed to have any current loops containing capacitors\n" + 
		"                        but no resistance. For example, capacitors connected in parallel are not allowed; you must put a\n" + 
		"                        resistor in series with them. Shorted capacitors are allowed.</li>\n" + 
		"                    <li style=\"text-align: justify;\">Singular matrix! - this means that your circuit is inconsistent (two different voltage sources connected\n" + 
		"                        to each other), or that the voltage at some point is undefined. It might mean that some component&#39;s\n" + 
		"                        terminals are unconnected; for example, if you create an op-amp but haven&#39;t connected anything\n" + 
		"                        to it yet, you will get this error.</li>\n" + 
		"                    <li style=\"text-align: justify;\">Convergence failed! - this means the simulator can&#39;t figure out what the state of the circuit should\n" + 
		"                        be. Just click Reset and hopefully that should fix it. Your circuit might be too complicated, but\n" + 
		"                        this happens sometimes even with the examples.</li>\n" + 
		"                    <li style=\"text-align: justify;\">Transmission line delay too large! - the transmission line delay is too large compared to the timestep\n" + 
		"                        of the simulator, so too much memory would be required. Make the delay smaller.</li>\n" + 
		"                    <li style=\"text-align: justify;\">Need to ground transmission line! - the bottom two wires of a transmission line must always be grounded\n" + 
		"                        in this simulator.</li>\n" + 
		"                </ul>\n" + 
		"            </fieldset>\n" + 
		"<br>\n" + 
		"            <form action=\"#\" method=\"GET\">\n" + 
		"                <fieldset>\n" + 
		"                    <legend>\n" + 
		"                        <h2 id=\"p6\">License</h2>\n" + 
		"                    </legend>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The simulator is provided with no support or warranty. Absolutely no guarantee is provided of suitability\n" + 
		"                        for any purpose.</p>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">This program is free software; you can redistribute it and/or modify it under the terms of the GNU General\n" + 
		"                        Public License as published by the Free Software Foundation; either version 2 of the License, or\n" + 
		"                        (at your option) any later version.</p>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even\n" + 
		"                        the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General\n" + 
		"                        Public License for more details.</p>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">For details of licensing see http://www.gnu.org/licenses/.</p>\n" + 
		"<br>\n" +
		"<hr>\n" +
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\"> Original by Paul Falstad. (http://www.falstad.com/)</p>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">JavaScript conversion by Iain Sharp. (http://lushprojects.com/)</p>\n" + 
		"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">The program was ported to Windows, MAC OS and Linux by Usevalad Khatkevich. (https://github.com/CEBA77)</p>\n" + 
		"                </fieldset>" +
		"<br />");
		
		HTML contentsRU = new HTML("<h3>Предупреждение! Если вы работаете с программой с помощью клавиатуры, то после любого взаимодействия (кроме прокрутки) с боковой панелью щелкните левой (или правой) кнопкой мыши на пустой области верхней панели. Исчезновение функциональности клавиш после взаимодействия с боковой панелью является особенностью JavaScript, которая интерпретируется в оболочке NW.js.</h3>\n" + 
			"<h3>Програма работает только с английской раскладкой клавиатуры.</h3><hr>\n" + 
			"        <h2>Содержание:</h2>\n" + 
			"    <ol type=l>\n" + 
			"        <li>\n" + 
			"            <p>\n" + 
			"                <a href=\"#h1\">Как пользоваться программой</a>\n" + 
			"            </p>\n" + 
			"            <li>\n" + 
			"                <p>\n" + 
			"                    <a href=\"#h2\">Рисование и редактирование схем</a>\n" + 
			"                </p>\n" + 
			"                <li>\n" + 
			"                    <p>\n" + 
			"                        <a href=\"#h3\">Высокочастотные схемы</a>\n" + 
			"                    </p>\n" + 
			"                    <li>\n" + 
			"                        <p>\n" + 
			"                            <a href=\"#h4\">Моделирование - не реальность</a>\n" + 
			"                        </p>\n" + 
			"                        <li>\n" + 
			"                            <p>\n" + 
			"                                <a href=\"#h5\">Некоторые ошибки</a>\n" + 
			"                            </p>\n" + 
			"                            <li>\n" + 
			"                                <p>\n" + 
			"                                    <a href=\"#h6\">Лицензия</a>\n" + 
			"                                </p>\n" + 
			"    </ol>\n" + 
			"    <h4>Основная часть информации взята из http://lushprojects.com/circuitjs/\n" + 
			"    </h4>\n" + 
			"    <form action=\"#\" method=\"GET\">\n" + 
			"        <fieldset>\n" + 
			"            <legend>\n" + 
			"                <h2 id=\"h1\">Как пользоваться программой</h2>\n" + 
			"            </legend>\n" + 
			"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Когда программа запускается, на экране появляется анимированная схема LRC-контура. Зеленый цвет указывает на положительное напряжение. Серый цвет указывает на землю. Красный цвет показывает отрицательное напряжение. Движущиеся желтые точки указывают ток.</p>\n" + 
			"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Чтобы включить или выключить переключатель, просто нажмите на него. Если вы переместите указатель мыши на любой компонент схемы, вы увидите краткое описание этого компонента и его текущее состояние в правом нижнем углу окна. Чтобы изменить компонент, наведите указатель мыши на него, щелкните правой кнопкой мыши (или нажмите &quot;Control&quot;, если у вас есть Mac) и выберите &quot;Изменить&quot;. Вы также можете получить доступ к функции редактирования, дважды щелкнув на компонент.</p>\n" + 
			"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">В нижней части окна есть три графика; они действуют как осциллографы, каждый из которых показывает напряжение и ток по конкретному компоненту. Напряжение отображается зеленым, а ток - желтым. Ток на графике может быть не виден, если график напряжения перекрывает график тока. Также показано пиковое значение напряжения. Переместив указатель мыши на один из графиков, компонент, который он представляет, будет подсвечен в схеме определенным цветом. Чтобы изменить или удалить область, щелкните правой кнопкой мыши на ней и в контекстном меню выберите &quot;Удалить&quot;. В этом контекстном меню также есть много других возможностей графиков. Чтобы просмотреть компонент в графике, щелкните правой кнопкой мыши над компонентом и выберите &quot;Подключить осциллограф&quot;.</p>\n" + 
			"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Меню &quot;Схемы&quot; содержит множество примеров схем.</p>\n" + 
			"            <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Некоторые схемы, например Основы->Потенциометр, содержат потенциометры или источники переменного напряжения. Их можно настроить с помощью ползунков, которые добавляются в боковую панель, или путем установки указателя мыши на компонент и с помощью колеса мыши.</p>\n" + 
			"        </fieldset>\n" + 
			"<br>\n" + 
			"        <form action=\"#\" method=\"GET\">\n" + 
			"            <fieldset>\n" + 
			"                <legend>\n" + 
			"                    <h2 id=\"h2\">Рисование и редактирование схем</h2>\n" + 
			"                </legend>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Вы можете получить пустую схему, выбрав «Пустая схема» в меню «Схемы». Для запуска симулятора вам нужно будет добавить хотя бы один источник напряжения.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Чтобы добавить компоненты или провод, выберите один из компонентов в меню «Рисовать». Обратите внимание, что основные компоненты имеют привязку к клавишам для быстрого добавления.  Когда в режиме добавления курсор меняется на &quot;+&quot;, кликните левой кнопкой мыши и перетащите курсор с одной точки на другую не отпуская левую кнопку мыши, чтобы добавить компонент.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Компоненты могут быть перемещены в режиме выбора (в режиме выбора &quot;+&quot; меняется на стрелку). Выберите &quot;Выбрать/Переместить&quot; из \"Рисовать\" или нажмите «Пробел» или нажмите «Esc», для перехода в режим выбора. При наведении курсора на компонент, он будет выделен определенным цветом и информация об этом компоненте будет отображена в области информации. Щелчок и перетаскивание компонента переместит компонент. Если вы нажмете на квадратные точки, иммитирующие соединения и перетащите или нажмете на элемент удерживая клавишу ctrl, это изменит размер компонента и переместит клемы.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Провода соединяются только на концах, а не посередине, поэтому вы должны провести каждый сегмент провода отдельно. Если симулятор видит не связанные точки, которые, по его мнению, вы собираетесь подключить, он выделяет их красным кружком.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Многие компоненты имеют настройки, которые могут быть выполнены с помощью функции редактирования, описанной выше. Для резисторов, конденсаторов и катушек индуктивности будет удобно устанавливать значение путем прокрутки колеса мыши при наведении на эти компоненты</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Меню «Файл» позволяет вам импортировать или экспортировать файлы схем.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Кнопка Restart сбрасывает цепь в исходное состояние. Если симуляция приостановлена, то дважды нажмите кнопку «Restart», чтобы перезапустить ее. Кнопка Start/Stop позволяет приостановить симуляцию. Ползунок «Скорость симуляции» позволяет вам регулировать скорость моделирования. Если симуляция не зависит от времени (то есть, если нет конденсаторов, индукторов или источников напряжения, зависящих от времени), то это не будет иметь никакого эффекта. Ползунок &quot;Скорость тока&quot; позволяет вам регулировать скорость точек, изображающих ток, т.к. если токи очень слабы (или сильны), то точки движутся слишком медленно (или слишком быстро).</p>\n" + 
			"            </fieldset>\n" + 
			"        </form>\n" + 
			"<br>\n" + 
			"        <form action=\"#\" method=\"GET\">\n" + 
			"            <fieldset>\n" + 
			"                <legend>\n" + 
			"                    <h2 id=h3>Высокочастотные схемы</h2>\n" + 
			"                </legend>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Эта программа симулирует схему, используя серию коротких временных шагов. На каждом шаге изменения напряжений и токов в цепи рассчитываются на основе моделей компонентов и состояния текущей схемы. Чтобы этот процесс работал, используемые временные шаги должны быть значительно короче, чем продолжительность любого события, представляющего интерес для схемы. Или, если хотите, временные шаги должны быть значительно короче, чем период наибольшего частотного сигнала, представляющего интерес.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">По умолчанию симулятор использует время шага 5&micro;s. Это нормально для звуковых частотных сигналов, но не для радиочастотных сигналов или быстрых цифровых сигналов. Время шага может быть изменено во вкладке «Другие настройки...» в меню &quot;Опции&quot;. Для сравнения, пример линии передачи в приложении использует время шага 5ps.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Время шага моделирования не следует путать с «Скорость симуляции» в правой панели. Время шага моделирования определяет промежутки каждого шага (в симулированном времени). «Скорость симуляции» контролирует, как часто (в режиме реального времени) компьютер вычисляет шаг.</p>\n" + 
			"\n" + 
			"            </fieldset>\n" + 
			"        </form>\n" + 
			"<br>\n" + 
			"        <form action=\"#\" method=\"GET\">\n" + 
			"            <fieldset>\n" + 
			"                <legend>\n" + 
			"                    <h2 id=h4>Моделирование - не реальность</h2>\n" + 
			"                </legend>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Физическое моделирование не является реальностью и не предполагает, что симуляция и реальность идентичны! Эта симуляция идеализирует многие компоненты. Провода и компоненты не имеют сопротивления. Источники напряжения идеальны - они будут пытаться подавать бесконечный ток, если вы им позволяете. Конденсаторы и катушки на 100% эффективны. Входы логических затворов потребляют нулевой ток - не так уж плохо в качестве приближения для КМОП логики, но не типичны, например, для ТТЛ 1980-х годов. Во что бы то ни стало, используйте этот симулятор, чтобы визуализировать схемы, но всегда проверяйте на практике.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Симулятор лишь численно аппроксимирует модели компонентов, которые также приблизительны. Даже без учета каких-либо ошибок, это просто грубый гайд к реальности. Этот симулятор может быть полезен для визуализации, но порой используется неправильным образом, т.к. любой симулятор может дать ложное чувство безопасности. Некоторые люди действительно не понимают эту важную концепцию. У меня даже был один пользователь, обвиняющий симулятор во «лжи» просто потому что он (или она) не учитывал идеализацию компонентов и не понимал фактической производительности компонентов, которые он выбрал для использования. Для всех инженеров, связанных с электроникой, важно, чтобы они всегда были полностью осведомлены о характеристиках компонента (и системы) в реальном мире и о том, как они отличаются от любого конкретного симулятора, который они используют. Если вам нужны более точные модели реальных компонентов, то симуляторы на SPICE являются гораздо более подходящими инструментами, чем этот, но даже тогда вам следует знать об отклонениях от реальности.</p>\n" + 
			"                <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Одним из следствий использования идеальных компонентов является то, что симулятор не сходится к результату для цепей, которые не имеют определенного поведения - например, идеальный источник напряжения, закоротившийся на идеальном проводе. Другой ситуацией, которая не может быть смоделирована в этих предположениях, является распределение тока между проводниками, если два совершенных проводника соединены параллельно. При использовании симулятора вы должны учитывать места, где реальная электроника отличается от идеала.</p>\n" + 
			"            </fieldset>\n" + 
			"        </form>\n" + 
			"<br>\n" + 
			"        <form action=\"#\" method=\"GET\">\n" + 
			"            <fieldset>\n" + 
			"                <legend>\n" + 
			"                    <h2 id=\"h5\">Некоторые ошибки</h2>\n" + 
			"                </legend>\n" + 
			"                <p style=\"font-family: Arial, Helvetica, sans-serif;\">Вот некоторые ошибки, которые могут возникнуть:</p>\n" + 
			"                <ul style=\"margin-top: 0px; margin-bottom: 0px; font-family: Arial, Helvetica, sans-serif;\">\n" + 
			"                    <li style=\"text-align: justify;\">Voltage source loop with no resistance! - это означает, что один из источников напряжения в вашей цепи закорочен. Удостоверьтесь, что у каждого источника напряжения есть определенное сопротивление</li>\n" + 
			"                    <li style=\"text-align: justify;\">Capacitor loop with no resistance! - не допускается наличие каких-либо токовых контуров, содержащих конденсаторы, но никакого сопротивления. Например, конденсаторы, подключенные параллельно, не допускаются; вы должны установить резистор последовательно с ними. В противном случае конденсаторы считаются закорочеными.</li>\n" + 
			"                    <li style=\"text-align: justify;\">Singular matrix! - это означает, что ваша цепь несовместима (два разных источника напряжения подключены друг к другу) или что напряжение в какой-либо точке не определено. Это может означать, что клемы некоторых компонентов не подключены; например, если вы создаете операционный усилитель, но еще ничего не подключили к нему, вы получите эту ошибку.</li>\n" + 
			"                    <li style=\"text-align: justify;\">Convergence failed! - это означает, что симулятор не может понять, каково должно быть состояние цепи. Просто нажмите «Restart» и, возможно, это исправиться. Ваша схема может быть слишком сложной. Иногда это случается даже с примерами.</li>\n" + 
			"                    <li style=\"text-align: justify;\">Transmission line delay too large! - задержка линии передачи слишком велика по сравнению с тайм-аутом симулятора, поэтому потребуется слишком много памяти. Уменьшите задержку.</li>\n" + 
			"                    <li style=\"text-align: justify;\">Need to ground transmission line! - нижние два провода линии передачи всегда должны быть заземлены в этом симуляторе.</li>\n" + 
			"                </ul>\n" + 
			"            </fieldset>\n" + 
			"<br>\n" + 
			"            <form action=\"#\" method=\"GET\">\n" + 
			"                <fieldset>\n" + 
			"                    <legend>\n" + 
			"                        <h2 id=\"h6\">Лицензия</h2>\n" + 
			"                    </legend>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Симулятор не имеет поддержки или гарантии. Абсолютно никакая гарантия не предусматривает пригодность для каких-либо целей.</p>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Это свободная программа: вы можете перераспространять ее и/или изменять ее на условиях Стандартной общественной лицензии GNU в том виде, в каком она была опубликована Фондом свободного программного обеспечения; либо версии 2 лицензии, либо (по вашему выбору) любой более поздней версии.</p>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Эта программа распространяется в надежде, что она будет полезной, но БЕЗО ВСЯКИХ ГАРАНТИЙ; даже без неявной гарантии ТОВАРНОГО ВИДА или ПРИГОДНОСТИ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Подробнее см. в Стандартной общественной лицензии GNU.</p>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Вы должны были получить копию Стандартной общественной лицензии GNU вместе с этой программой. Если это не так, см. http://www.gnu.org/licenses/.</p>\n" + 
			"                    <br>\n" + 
			"                    <hr>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Оригинал: Paul Falstad. (http://www.falstad.com/)</p>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Компиляция в JavaScript: Iain Sharp. (http://lushprojects.com/)</p>\n" + 
			"                    <p align=\"justify\" style=\"font-family: Arial, Helvetica, sans-serif;\">Портирование программы на Windows, MAC OS и Linux: Хаткевич Всеволод. (https://github.com/CEBA77)</p>\n");
			
		
		
		
		TabPanel tp = new TabPanel();
		vp.add(tp);
		String tab1Title = "EN";
		String tab2Title = "RU";

		      //create tabs 
		tp.add(sp= new ScrollPanel(contentsEN), tab1Title);
		tp.add(sp2= new ScrollPanel(contentsRU), tab2Title);
		
		sp.setWidth("500px");
		sp.setHeight("400px");
		
		sp2.setWidth("500px");
		sp2.setHeight("400px");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hp.setStyleName("topSpace");
		
		//select first tab
		tp.selectTab(0);

		//set width if tabpanel
		tp.setWidth("500");
		
		vp.add(hp);
		hp.add(okButton = new Button("OK"));
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				closeDialog();
			}
		});
		center();
		show();
	}

	protected void closeDialog() {
		hide();
	}
}
