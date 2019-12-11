package tccthainan;

import guarana.framework.message.Exchange;
import guarana.framework.message.Message;
import guarana.framework.task.Task;
import guarana.toolkit.task.communicators.dummy.InDummyCommunicator;
import guarana.framework.task.Slot;
import guarana.toolkit.task.routers.Filter;
import guarana.framework.port.EntryPort;
import guarana.framework.port.SolicitorPort;
import guarana.framework.port.ExitPort;
import guarana.framework.port.OneWayPort;
import guarana.framework.port.TwoWayPort;
import guarana.framework.process.Process;
import guarana.framework.task.TaskExecutionException;
import guarana.toolkit.task.communicators.dummy.OutDummyCommunicator;
import guarana.toolkit.task.communicators.dummy.OutInDummyCommunicator;
import guarana.toolkit.task.modifiers.ContextBasedContentEnricher;
import guarana.toolkit.task.modifiers.Slimmer;
import guarana.toolkit.task.routers.Correlator;
import guarana.toolkit.task.routers.Dispatcher;
import guarana.toolkit.task.routers.Merger;
import guarana.toolkit.task.routers.Replicator;
import guarana.toolkit.task.transformers.Aggregator;
import guarana.toolkit.task.transformers.Splitter;
import guarana.toolkit.task.transformers.Transformer;
import guarana.toolkit.task.transformers.Translator;
import guarana.util.xml.XMLHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Thainan
 */
public class Integration extends Process {

    private Task[] task;
    private Slot[] slot;
    private OneWayPort entryPort, exitPort;
    private TwoWayPort creditAgencyPort, rulesPort, bankXPort, bankYPort, bankZPort;
    public InDummyCommunicator communicatorEntry;

    public Integration() {

        super("Integration Process");

        // LISTA DE SLOTS
        slot = new Slot[40];
        for (int i = 0; i < slot.length; i++) {
            slot[i] = new Slot("Slot " + i);

        }

        // LISTA DE TASKS
        task = new Task[40];

        // ##########################PORTS###############################
        // ENTRY PORT
        entryPort = new EntryPort("Entry Port") {
            @Override
            public void initialise() {
                setInterSlot(new Slot("InterSlot"));

                // **** Communicator
                communicatorEntry = new InDummyCommunicator("Communicator@User");

                communicatorEntry.output[0].bind(getInterSlot());

                setCommunicator(communicatorEntry);

            }

        };
        addPort(entryPort);

        // EXIT PORT
        exitPort = new ExitPort("Exit Port") {
            @Override
            public void initialise() {
                setInterSlot(new Slot("InterSlot"));

                // **** Communicator - File Adapter
                Task communicator = new OutDummyCommunicator("Communicator@Exit") {
                    @Override
                    public void execute() throws TaskExecutionException {
                        //super.execute();
                        Message<Document> msg = (Message<Document>) input[0].getMessage();
                        XMLHandler.writeXmlFile(msg.getBody(), "exit.xml");
                        System.out.println("Mensagem chegou na porta de saída");

                    }

                };

                communicator.input[0].bind(getInterSlot());
                setCommunicator(communicator);

            }
        };
        addPort(exitPort);

        // CREDIT AGENCY PORT
        creditAgencyPort = new SolicitorPort("Credit Agency Port") {

            public void initialise() {
                setInterSlotIn(new Slot("InterSlot In"));
                setInterSlotOut(new Slot("InterSlot Out"));

                Task communicator = new OutInDummyCommunicator("OutIn") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void doWork(Exchange exchange) throws TaskExecutionException {

                        Message<Document> request = (Message<Document>) exchange.input[0].poll();
                        //x2                        
                        Document msg2 = (Document) request.getBody();

                        //x3
                        Document msg3 = XMLHandler.newDocument();

                        if (msg2.getElementsByTagName("cpf").item(0).getTextContent().equals("00000000000")) {

                            Element root = msg3.createElement("person");
                            msg3.appendChild(root);

                            //CPF
                            Element cpf = msg3.createElement("cpf");
                            cpf.setTextContent(msg2.getElementsByTagName("cpf").item(0).getTextContent());
                            root.appendChild(cpf);

                            //Score
                            Element score = msg3.createElement("score");
                            score.setTextContent("800");
                            root.appendChild(score);

                            //Histórico
                            Element historic = msg3.createElement("historic");
                            historic.setTextContent("positive");
                            root.appendChild(historic);

                            Message<Document> outMsg = new Message<Document>(request);
                            outMsg.setBody(msg3);

                            XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/credit_agency_port.xml");
                            Message<Document> outMsgInterface = new Message<Document>(request);
                            outMsgInterface.setBody(msg3);

                            exchange.output[0].add(outMsgInterface);

                        }

                        if (msg2.getElementsByTagName("cpf").item(0).getTextContent().equals("11111111111")) {

                            Element root = msg3.createElement("person");

                            msg3.appendChild(root);

                            //CPF
                            Element cpf = msg3.createElement("cpf");
                            cpf.setTextContent(msg2.getElementsByTagName("cpf").item(0).getTextContent());
                            root.appendChild(cpf);

                            //Score
                            Element score = msg3.createElement("score");
                            score.setTextContent("500");
                            root.appendChild(score);

                            //Histórico
                            Element historic = msg3.createElement("historic");
                            historic.setTextContent("positive");
                            root.appendChild(historic);

                            Message<Document> outMsg = new Message<Document>(request);
                            outMsg.setBody(msg3);

                            XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/credit_agency_port.xml");
                            Message<Document> outMsgInterface = new Message<Document>(request);
                            outMsgInterface.setBody(msg3);

                            exchange.output[0].add(outMsgInterface);

                        }

                        if (msg2.getElementsByTagName("cpf").item(0).getTextContent().equals("22222222222")) {

                            Element root = msg3.createElement("person");

                            msg3.appendChild(root);

                            //CPF
                            Element cpf = msg3.createElement("cpf");
                            cpf.setTextContent(msg2.getElementsByTagName("cpf").item(0).getTextContent());
                            root.appendChild(cpf);

                            //Score
                            Element score = msg3.createElement("score");
                            score.setTextContent("250");
                            root.appendChild(score);

                            //Histórico
                            Element historic = msg3.createElement("historic");
                            historic.setTextContent("negative");
                            root.appendChild(historic);

                            Message<Document> outMsg = new Message<Document>(request);
                            outMsg.setBody(msg3);

                            XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/credit_agency_port_X2.xml");
                            Message<Document> outMsgInterface = new Message<Document>(request);
                            outMsgInterface.setBody(msg3);

                            exchange.output[0].add(outMsgInterface);

                        }
                    }
                };

                communicator.input[0].bind(getInterSlotIn());
                communicator.output[0].bind(getInterSlotOut());
                setCommunicator(communicator);
            }
        };

        addPort(creditAgencyPort);

        // RULES PORT
        rulesPort = new SolicitorPort("Rules Port") {

            public void initialise() {
                setInterSlotIn(new Slot("InterSlot In"));
                setInterSlotOut(new Slot("InterSlot Out"));

                Task communicator = new OutInDummyCommunicator("OutIn") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void doWork(Exchange exchange) throws TaskExecutionException {

                        Message<Document> request = (Message<Document>) exchange.input[0].poll();

                        Document message = request.getBody();

                        Document x4 = XMLHandler.newDocument();

                        Element root = x4.createElement("baseRulesPort");
                        x4.appendChild(root);

                        Element cpf = x4.createElement("cpf");
                        cpf.setTextContent(message.getElementsByTagName("cpf").item(0).getTextContent());
                        root.appendChild(cpf);

                        Element banks = x4.createElement("banks");
                        root.appendChild(banks);

                        // A -> histórico positivo/negativo;
                        // B -> cada parcela do emprestimo deve ser menor ou igual que 30% da renda bruta;
                        // C -> fiador: algumas agências exigem a indicação de pelo menos um fiador para concessão do empréstimo;
                        // D -> possuir bem patrimonial como garantia. 
                        // BANCO X - C e D
                        // BANCO Y - B
                        // BANCO Z - A
                        boolean A;
                        boolean B;
                        boolean C;
                        boolean D;

                        // REGRA A
                        String historico = message.getElementsByTagName("historic").item(0).getTextContent();

                        if (historico.equals("positive")) {
                            A = true;
                        } else {
                            A = false;
                        }

                        // REGRA B
                        Integer parcelas = Integer.valueOf(message.getElementsByTagName("instalment").item(0).getTextContent());
                        Integer salario = Integer.valueOf(message.getElementsByTagName("income").item(0).getTextContent());
                        Integer total = Integer.valueOf(message.getElementsByTagName("value").item(0).getTextContent());

                        if (total / parcelas <= (salario / 100) * 30) {
                            B = true;
                        } else {
                            B = false;
                        }

                        // REGRA C
                        String fiador = message.getElementsByTagName("fiador").item(0).getTextContent();

                        if (!fiador.isBlank()) {
                            C = true;
                        } else {
                            C = false;
                        }

                        // REGRA D
                        String garantia = message.getElementsByTagName("garantia").item(0).getTextContent();

                        if (garantia.equals("sim")) {
                            D = true;
                        } else {
                            D = false;
                        }

                        // BANCO X
                        if (C & D == true) {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("bankX");

                        } else {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("");

                        }

                        // BANCO Y
                        if (B == true) {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("bankY");

                        } else {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("");

                        }

                        // BANCO Z
                        if (A == true) {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("bankZ");

                        } else {

                            Element bank = x4.createElement("bank");
                            banks.appendChild(bank);
                            bank.setTextContent("");

                        }

                        Message<Document> outMsg = new Message<Document>();
                        outMsg.setBody(x4);

                        XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/rules_port_X4.xml");

                        exchange.output[0].add(outMsg);

                    }
                };

                communicator.input[0].bind(getInterSlotIn());
                communicator.output[0].bind(getInterSlotOut());
                setCommunicator(communicator);
            }
        };
        addPort(rulesPort);

        // BANK X PORT
        bankXPort = new SolicitorPort("bank X Port") {

            public void initialise() {
                setInterSlotIn(new Slot("InterSlot In"));
                setInterSlotOut(new Slot("InterSlot Out"));

                Task communicator = new OutInDummyCommunicator("OutIn") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void doWork(Exchange exchange) throws TaskExecutionException {

                        Message<Document> request = (Message<Document>) exchange.input[0].poll();

                        Document x6 = (Document) request.getBody();

                        Document x9 = XMLHandler.newDocument();

                        Element root = x9.createElement(x6.getFirstChild().getNodeName());
                        x9.appendChild(root);

                        Element score = x9.createElement("score");
                        score.setTextContent(x6.getElementsByTagName("score").item(0).getTextContent());
                        root.appendChild(score);

                        Element historic = x9.createElement("historic");
                        historic.setTextContent(x6.getElementsByTagName("historic").item(0).getTextContent());
                        root.appendChild(historic);

                        Element name = x9.createElement("name");
                        name.setTextContent(x6.getElementsByTagName("name").item(0).getTextContent());
                        root.appendChild(name);

                        Element adress = x9.createElement("adress");
                        adress.setTextContent(x6.getElementsByTagName("adress").item(0).getTextContent());
                        root.appendChild(adress);

                        Element cpf = x9.createElement("cpf");
                        cpf.setTextContent(x6.getElementsByTagName("cpf").item(0).getTextContent());
                        root.appendChild(cpf);

                        Element birth = x9.createElement("birth");
                        birth.setTextContent(x6.getElementsByTagName("birth").item(0).getTextContent());
                        root.appendChild(birth);

                        Element income = x9.createElement("income");
                        income.setTextContent(x6.getElementsByTagName("income").item(0).getTextContent());
                        root.appendChild(income);

                        Element value = x9.createElement("value");
                        value.setTextContent(x6.getElementsByTagName("value").item(0).getTextContent());
                        root.appendChild(value);

                        Element instalment = x9.createElement("instalment");
                        instalment.setTextContent(x6.getElementsByTagName("instalment").item(0).getTextContent());
                        root.appendChild(instalment);

                        Element fiador = x9.createElement("fiador");
                        fiador.setTextContent(x6.getElementsByTagName("fiador").item(0).getTextContent());
                        root.appendChild(fiador);

                        Element garantia = x9.createElement("garantia");
                        garantia.setTextContent(x6.getElementsByTagName("garantia").item(0).getTextContent());
                        root.appendChild(garantia);

                        Element bank = x9.createElement("bank");
                        bank.setTextContent("bankX");
                        root.appendChild(bank);

                        Element interestRate = x9.createElement("interestRate");
                        interestRate.setTextContent("5");
                        root.appendChild(interestRate);

                        Message<Document> outMsg = new Message<Document>();
                        outMsg.setBody(x9);

                        XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/bankX_port_X9.xml");

                        exchange.output[0].add(outMsg);

                    }
                };

                communicator.input[0].bind(getInterSlotIn());
                communicator.output[0].bind(getInterSlotOut());
                setCommunicator(communicator);
            }
        };
        addPort(bankXPort);

        // BANK Y PORT
        bankYPort = new SolicitorPort("bank Y Port") {

            public void initialise() {
                setInterSlotIn(new Slot("InterSlot In"));
                setInterSlotOut(new Slot("InterSlot Out"));

                Task communicator = new OutInDummyCommunicator("OutIn") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void doWork(Exchange exchange) throws TaskExecutionException {

                        Message<Document> request = (Message<Document>) exchange.input[0].poll();

                        Document x7 = (Document) request.getBody();

                        Document x10 = XMLHandler.newDocument();

                        Element root = x10.createElement(x7.getFirstChild().getNodeName());
                        x10.appendChild(root);

                        Element score = x10.createElement("score");
                        score.setTextContent(x7.getElementsByTagName("score").item(0).getTextContent());
                        root.appendChild(score);

                        Element historic = x10.createElement("historic");
                        historic.setTextContent(x7.getElementsByTagName("historic").item(0).getTextContent());
                        root.appendChild(historic);

                        Element name = x10.createElement("name");
                        name.setTextContent(x7.getElementsByTagName("name").item(0).getTextContent());
                        root.appendChild(name);

                        Element adress = x10.createElement("adress");
                        adress.setTextContent(x7.getElementsByTagName("adress").item(0).getTextContent());
                        root.appendChild(adress);

                        Element cpf = x10.createElement("cpf");
                        cpf.setTextContent(x7.getElementsByTagName("cpf").item(0).getTextContent());
                        root.appendChild(cpf);

                        Element birth = x10.createElement("birth");
                        birth.setTextContent(x7.getElementsByTagName("birth").item(0).getTextContent());
                        root.appendChild(birth);

                        Element income = x10.createElement("income");
                        income.setTextContent(x7.getElementsByTagName("income").item(0).getTextContent());
                        root.appendChild(income);

                        Element value = x10.createElement("value");
                        value.setTextContent(x7.getElementsByTagName("value").item(0).getTextContent());
                        root.appendChild(value);

                        Element instalment = x10.createElement("instalment");
                        instalment.setTextContent(x7.getElementsByTagName("instalment").item(0).getTextContent());
                        root.appendChild(instalment);

                        Element fiador = x10.createElement("fiador");
                        fiador.setTextContent(x7.getElementsByTagName("fiador").item(0).getTextContent());
                        root.appendChild(fiador);

                        Element garantia = x10.createElement("garantia");
                        garantia.setTextContent(x7.getElementsByTagName("garantia").item(0).getTextContent());
                        root.appendChild(garantia);

                        Element bank = x10.createElement("bank");
                        bank.setTextContent("bankY");
                        root.appendChild(bank);

                        Element interestRate = x10.createElement("interestRate");
                        interestRate.setTextContent("10");
                        root.appendChild(interestRate);

                        Message<Document> outMsg = new Message<Document>();
                        outMsg.setBody(x10);

                        XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/bankY_port_X10.xml");

                        exchange.output[0].add(outMsg);

                    }
                };

                communicator.input[0].bind(getInterSlotIn());
                communicator.output[0].bind(getInterSlotOut());
                setCommunicator(communicator);
            }
        };
        addPort(bankYPort);

        // BANK Z PORT
        bankZPort = new SolicitorPort("bank Z Port") {

            public void initialise() {
                setInterSlotIn(new Slot("InterSlot In"));
                setInterSlotOut(new Slot("InterSlot Out"));

                Task communicator = new OutInDummyCommunicator("OutIn") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void doWork(Exchange exchange) throws TaskExecutionException {

                        Message<Document> request = (Message<Document>) exchange.input[0].poll();

                        Document x8 = (Document) request.getBody();

                        Document x11 = XMLHandler.newDocument();

                        Element root = x11.createElement(x8.getFirstChild().getNodeName());
                        x11.appendChild(root);

                        Element score = x11.createElement("score");
                        score.setTextContent(x8.getElementsByTagName("score").item(0).getTextContent());
                        root.appendChild(score);

                        Element historic = x11.createElement("historic");
                        historic.setTextContent(x8.getElementsByTagName("historic").item(0).getTextContent());
                        root.appendChild(historic);

                        Element name = x11.createElement("name");
                        name.setTextContent(x8.getElementsByTagName("name").item(0).getTextContent());
                        root.appendChild(name);

                        Element adress = x11.createElement("adress");
                        adress.setTextContent(x8.getElementsByTagName("adress").item(0).getTextContent());
                        root.appendChild(adress);

                        Element cpf = x11.createElement("cpf");
                        cpf.setTextContent(x8.getElementsByTagName("cpf").item(0).getTextContent());
                        root.appendChild(cpf);

                        Element birth = x11.createElement("birth");
                        birth.setTextContent(x8.getElementsByTagName("birth").item(0).getTextContent());
                        root.appendChild(birth);

                        Element income = x11.createElement("income");
                        income.setTextContent(x8.getElementsByTagName("income").item(0).getTextContent());
                        root.appendChild(income);

                        Element value = x11.createElement("value");
                        value.setTextContent(x8.getElementsByTagName("value").item(0).getTextContent());
                        root.appendChild(value);

                        Element instalment = x11.createElement("instalment");
                        instalment.setTextContent(x8.getElementsByTagName("instalment").item(0).getTextContent());
                        root.appendChild(instalment);

                        Element fiador = x11.createElement("fiador");
                        fiador.setTextContent(x8.getElementsByTagName("fiador").item(0).getTextContent());
                        root.appendChild(fiador);

                        Element garantia = x11.createElement("garantia");
                        garantia.setTextContent(x8.getElementsByTagName("garantia").item(0).getTextContent());
                        root.appendChild(garantia);

                        Element bank = x11.createElement("bank");
                        bank.setTextContent("bankX");
                        root.appendChild(bank);

                        Element interestRate = x11.createElement("interestRate");
                        interestRate.setTextContent("15");
                        root.appendChild(interestRate);

                        Message<Document> outMsg = new Message<Document>();
                        outMsg.setBody(x11);

                        XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/bankZ_port_X11.xml");

                        exchange.output[0].add(outMsg);

                    }
                };

                communicator.input[0].bind(getInterSlotIn());
                communicator.output[0].bind(getInterSlotOut());
                setCommunicator(communicator);
            }
        };
        addPort(bankZPort);

        // TASKS
        // REPLICATOR T1
        task[0] = new Replicator("REPLICATOR T1", 2);
        task[0].input[0].bind(entryPort.getInterSlot());
        task[0].output[0].bind(slot[0]);
        task[0].output[1].bind(slot[1]);
        addTask(task[0]);

        // TRANSLATOR T2
        task[1] = new Translator("TRANSLATOR T2") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                Document entry = inMsg.getBody();

                Document exit = XMLHandler.newDocument();
                // <MessageCredit>
                Element root = exit.createElement("toCreditAgencyPort");
                exit.appendChild(root);

                // <CPF>
                Element cpf = exit.createElement("cpf");
                cpf.setTextContent(entry.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(exit);
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T2_X1.xml");
                //DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[1].input[0].bind(slot[0]);
        task[1].output[0].bind(creditAgencyPort.getInterSlotIn());
        addTask(task[1]);

        // TRANSLATOR T3
        task[2] = new Translator("TRANSLATOR T3") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T3_X2.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[2].input[0].bind(creditAgencyPort.getInterSlotOut());
        task[2].output[0].bind(slot[2]);
        addTask(task[2]);

        // CORRELATOR T4
        task[3] = new Correlator("CORRELATOR T4", 2, 2) {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document msg1 = inMsg1.getBody();
                String cpf = msg1.getElementsByTagName("cpf").item(0).getTextContent();

                Document msg2 = inMsg2.getBody();
                String cpf2 = msg2.getElementsByTagName("cpf").item(0).getTextContent();

                if (cpf.equals(cpf2)) {

                    // DEBUG
                    Message<Document> outMsg1 = new Message<Document>(inMsg1);
                    outMsg1.setBody(inMsg1.getBody());

                    Message<Document> outMsg2 = new Message<Document>(inMsg2);
                    outMsg2.setBody(inMsg2.getBody());

                    XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T4_X0.xml");
                    XMLHandler.writeXmlFile(outMsg2.getBody(), "debugs/T4_X2.xml");
                    // DEBUG

                    exchng.output[0].add(inMsg1);
                    exchng.output[1].add(inMsg2);

                }
            }
        };
        task[3].input[0].bind(slot[1]);
        task[3].input[1].bind(slot[2]);
        task[3].output[0].bind(slot[3]);
        task[3].output[1].bind(slot[4]);
        addTask(task[3]);

        // CONTEXT BASED CONTENT ENRICHER T5
        task[4] = new ContextBasedContentEnricher("CONTEXT BASED ENRICHER T5") {
            @SuppressWarnings("unchecked")
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document x0 = inMsg1.getBody();
                Document x2 = inMsg2.getBody();

                Document x3 = XMLHandler.newDocument();

                Element root = x3.createElement("person");
                x3.appendChild(root);

                Element score = x3.createElement("score");
                score.setTextContent(x2.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x3.createElement("historic");
                historic.setTextContent(x2.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x3.createElement("name");
                name.setTextContent(x0.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x3.createElement("adress");
                adress.setTextContent(x0.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x3.createElement("cpf");
                cpf.setTextContent(x0.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x3.createElement("birth");
                birth.setTextContent(x0.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x3.createElement("income");
                income.setTextContent(x0.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x3.createElement("value");
                value.setTextContent(x0.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x3.createElement("instalment");
                instalment.setTextContent(x0.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element fiador = x3.createElement("fiador");
                fiador.setTextContent(x0.getElementsByTagName("fiador").item(0).getTextContent());
                root.appendChild(fiador);

                Element garantia = x3.createElement("garantia");
                garantia.setTextContent(x0.getElementsByTagName("garantia").item(0).getTextContent());
                root.appendChild(garantia);

                Message<Document> outMsg1 = new Message<Document>();
                outMsg1.setBody(x3);

                // DEBUG
                Message<Document> debugMsg1 = new Message<Document>(outMsg1);
                debugMsg1.setBody(inMsg2.getBody());

                XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T5_X3.xml");
                // DEBUG

                exchng.output[0].add(outMsg1);

            }
        };
        task[4].input[0].bind(slot[3]);
        task[4].input[1].bind(slot[4]);
        task[4].output[0].bind(slot[5]);
        addTask(task[4]);

        // FILTER T6
        task[5] = new Filter("FILTER T6") {
            @Override
            public void doWork(Exchange exchange) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchange.input[0].poll();

                Document msg1 = inMsg.getBody();

                String score = msg1.getElementsByTagName("score").item(0).getTextContent();

                if (Integer.parseInt(score) < 300) {

                    // DESCARTA SCORE MENOR QUE 300
                } else {

                    exchange.output[0].add(inMsg);
                    XMLHandler.writeXmlFile(inMsg.getBody(), "debugs/T6_X3.xml");
                }

            }
        };
        task[5].input[0].bind(slot[5]);
        task[5].output[0].bind(slot[6]);
        addTask(task[5]);

        // REPLICATOR T7
        task[6] = new Replicator("REPLICATOR T7", 2);
        task[6].input[0].bind(slot[6]);
        task[6].output[0].bind(slot[7]);
        task[6].output[1].bind(slot[8]);
        addTask(task[6]);

        // TRANSLATOR T8
        task[7] = new Translator("TRANSLATOR T8") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T8_X3.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[7].input[0].bind(slot[8]);
        task[7].output[0].bind(rulesPort.getInterSlotIn());
        addTask(task[7]);

        // TRANSLATOR T9
        task[8] = new Translator("TRANSLATOR T9") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T9_X4.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[8].input[0].bind(rulesPort.getInterSlotOut());
        task[8].output[0].bind(slot[9]);
        addTask(task[8]);

        // CORRELATOR T10
        task[9] = new Correlator("CORRELATOR T10", 2, 2) {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document msg1 = inMsg1.getBody();
                String cpf = msg1.getElementsByTagName("cpf").item(0).getTextContent();

                Document msg2 = inMsg2.getBody();
                String cpf2 = msg2.getElementsByTagName("cpf").item(0).getTextContent();

                if (cpf.equals(cpf2)) {

                    // DEBUG
                    Message<Document> outMsg1 = new Message<Document>(inMsg1);
                    outMsg1.setBody(inMsg1.getBody());

                    Message<Document> outMsg2 = new Message<Document>(inMsg2);
                    outMsg2.setBody(inMsg2.getBody());

                    XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T10_X3.xml");
                    XMLHandler.writeXmlFile(outMsg2.getBody(), "debugs/T10_X4.xml");
                    // DEBUG

                    exchng.output[0].add(inMsg1);
                    exchng.output[1].add(inMsg2);

                }
            }
        };
        task[9].input[0].bind(slot[7]);
        task[9].input[1].bind(slot[9]);
        task[9].output[0].bind(slot[10]);
        task[9].output[1].bind(slot[11]);
        addTask(task[9]);

        // CONTEXT BASED CONTENT ENRICHER T11
        task[10] = new ContextBasedContentEnricher("CONTEXT BASED ENRICHER T11") {
            @SuppressWarnings("unchecked")
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                // X3
                Document x3 = inMsg1.getBody();

                // X4
                Document x4 = inMsg2.getBody();

                // X5
                Document x5 = XMLHandler.newDocument();

                int count = 0;

                NodeList bankslist = x4.getElementsByTagName("bank");

                ArrayList banksArray = new ArrayList<String>();

                for (int i = 0; i < bankslist.getLength(); i++) {

                    Element d = (Element) bankslist.item(i);

                    banksArray.add(d.getTextContent());

                    count++;

                }

                Element root = x5.createElement("personwithbanks");
                x5.appendChild(root);

                Element score = x5.createElement("score");
                score.setTextContent(x3.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x5.createElement("historic");
                historic.setTextContent(x3.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x5.createElement("name");
                name.setTextContent(x3.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x5.createElement("adress");
                adress.setTextContent(x3.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x5.createElement("cpf");
                cpf.setTextContent(x3.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x5.createElement("birth");
                birth.setTextContent(x3.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x5.createElement("income");
                income.setTextContent(x3.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x5.createElement("value");
                value.setTextContent(x3.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x5.createElement("instalment");
                instalment.setTextContent(x3.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element fiador = x5.createElement("fiador");
                fiador.setTextContent(x3.getElementsByTagName("fiador").item(0).getTextContent());
                root.appendChild(fiador);

                Element garantia = x5.createElement("garantia");
                garantia.setTextContent(x3.getElementsByTagName("garantia").item(0).getTextContent());
                root.appendChild(garantia);

                Element banks = x5.createElement("banks");
                root.appendChild(banks);

                if (banksArray.get(0).equals("")) {

                } else {

                    Element bank = x5.createElement("bank");
                    bank.setTextContent(banksArray.get(0).toString());
                    banks.appendChild(bank);

                }

                if (banksArray.get(1).equals("")) {

                } else {

                    Element bank = x5.createElement("bank");
                    bank.setTextContent(banksArray.get(1).toString());
                    banks.appendChild(bank);

                }

                if (banksArray.get(2).equals("")) {

                } else {

                    Element bank = x5.createElement("bank");
                    bank.setTextContent(banksArray.get(2).toString());
                    banks.appendChild(bank);

                }

                Message<Document> outMsg1 = new Message<Document>();
                outMsg1.setBody(x5);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T11_X5.xml");
                // DEBUG

                exchng.output[0].add(outMsg1);

            }
        };
        task[10].input[0].bind(slot[10]);
        task[10].input[1].bind(slot[11]);
        task[10].output[0].bind(slot[12]);
        addTask(task[10]);

        // FILTER T12
        task[11] = new Filter("FILTER T12") {
            @Override
            public void doWork(Exchange exchange) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchange.input[0].poll();

                Document x5 = inMsg.getBody();

                NodeList bankslist = x5.getElementsByTagName("bank");

                ArrayList banksArray = new ArrayList<String>();

                for (int i = 0; i < bankslist.getLength(); i++) {

                    Element d = (Element) bankslist.item(i);

                    if (!d.getTextContent().equals("")) {

                        banksArray.add(d.getTextContent());

                    }

                }

                if (banksArray.isEmpty()) {
                    // DESCARTA LISTA DE BANCOS VAZIA
                } else {
                    XMLHandler.writeXmlFile(inMsg.getBody(), "debugs/T12_X5.xml");

                    exchange.output[0].add(inMsg);
                }

            }
        };
        task[11].input[0].bind(slot[12]);
        task[11].output[0].bind(slot[13]);
        addTask(task[11]);

        // SPLITTER T13
        task[12] = new Splitter("SPLITTER T13") {
            @Override
            public void doWork(Exchange exchange) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchange.input[0].poll();

                Document x5 = inMsg.getBody();

                NodeList banks = x5.getElementsByTagName("bank");

                for (Integer i = 1; i <= banks.getLength(); i++) {

                    Element bk = (Element) banks.item(i - 1);

                    Document docX5 = XMLHandler.newDocument();

                    Element rootX5 = docX5.createElement("SPLITTER" + i.toString() + Integer.toString(banks.getLength()));
                    docX5.appendChild(rootX5);

                    Element score = docX5.createElement("score");
                    score.setTextContent(x5.getElementsByTagName("score").item(0).getTextContent());
                    rootX5.appendChild(score);

                    Element historic = docX5.createElement("historic");
                    historic.setTextContent(x5.getElementsByTagName("historic").item(0).getTextContent());
                    rootX5.appendChild(historic);

                    Element name = docX5.createElement("name");
                    name.setTextContent(x5.getElementsByTagName("name").item(0).getTextContent());
                    rootX5.appendChild(name);

                    Element adress = docX5.createElement("adress");
                    adress.setTextContent(x5.getElementsByTagName("adress").item(0).getTextContent());
                    rootX5.appendChild(adress);

                    Element cpf = docX5.createElement("cpf");
                    cpf.setTextContent(x5.getElementsByTagName("cpf").item(0).getTextContent());
                    rootX5.appendChild(cpf);

                    Element birth = docX5.createElement("birth");
                    birth.setTextContent(x5.getElementsByTagName("birth").item(0).getTextContent());
                    rootX5.appendChild(birth);

                    Element income = docX5.createElement("income");
                    income.setTextContent(x5.getElementsByTagName("income").item(0).getTextContent());
                    rootX5.appendChild(income);

                    Element value = docX5.createElement("value");
                    value.setTextContent(x5.getElementsByTagName("value").item(0).getTextContent());
                    rootX5.appendChild(value);

                    Element instalment = docX5.createElement("instalment");
                    instalment.setTextContent(x5.getElementsByTagName("instalment").item(0).getTextContent());
                    rootX5.appendChild(instalment);

                    Element fiador = docX5.createElement("fiador");
                    fiador.setTextContent(x5.getElementsByTagName("fiador").item(0).getTextContent());
                    rootX5.appendChild(fiador);

                    Element garantia = docX5.createElement("garantia");
                    garantia.setTextContent(x5.getElementsByTagName("garantia").item(0).getTextContent());
                    rootX5.appendChild(garantia);

                    Element bank = docX5.createElement("bank");
                    bank.setTextContent(bk.getTextContent());
                    rootX5.appendChild(bank);

                    Message<Document> outMsg = new Message<Document>(inMsg);
                    outMsg.setBody(docX5);

                    XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T13_" + bk.getTextContent() + i.toString() + Integer.toString(banks.getLength()) + ".xml");

                    System.out.println("Splitter: " + bk.getTextContent() + " - " + outMsg.getHeader());

                    exchange.output[0].add(outMsg);

                }

            }
        };
        task[12].input[0].bind(slot[13]);
        task[12].output[0].bind(slot[14]);
        addTask(task[12]);

        // DISPATCHER T14
        task[13] = new Dispatcher("DISPATCHER T14", 1, 3) {
            @Override
            public void doWork(Exchange exchange) {

                Message<Document> inMsg = (Message<Document>) exchange.input[0].poll();

                Document msgHere = inMsg.getBody();

                if (msgHere.getElementsByTagName("bank").item(0).getTextContent().equals("bankX")) {

                    XMLHandler.writeXmlFile(inMsg.getBody(), "debugs/T14_BANKX.xml");
                    exchange.output[0].add(inMsg);

                }

                if (msgHere.getElementsByTagName("bank").item(0).getTextContent().equals("bankY")) {

                    XMLHandler.writeXmlFile(inMsg.getBody(), "debugs/T14_BANKY.xml");
                    exchange.output[1].add(inMsg);

                }

                if (msgHere.getElementsByTagName("bank").item(0).getTextContent().equals("bankZ")) {

                    XMLHandler.writeXmlFile(inMsg.getBody(), "debugs/T14_BANKZ.xml");
                    exchange.output[2].add(inMsg);

                }

            }
        };
        task[13].input[0].bind(slot[14]);
        task[13].output[0].bind(slot[15]); // BANK X
        task[13].output[1].bind(slot[16]); // BANK Y
        task[13].output[2].bind(slot[17]); // BANK Z
        addTask(task[13]);

        // BANCO X
        // REPLICATOR T15
        task[14] = new Replicator("REPLICATOR T15", 2);
        task[14].input[0].bind(slot[15]);
        task[14].output[0].bind(slot[18]); // VAI PARA O TRANSLATOR T16
        task[14].output[1].bind(slot[19]); // VAI PARA O CORRELATOR T18
        addTask(task[14]);

        // TRANSLATOR T16
        task[15] = new Translator("TRANSLATOR T16") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T16_X6.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[15].input[0].bind(slot[18]);
        task[15].output[0].bind(bankXPort.getInterSlotIn());
        addTask(task[15]);

        // TRANSLATOR T17
        task[16] = new Translator("TRANSLATOR T17") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T17_X9.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[16].input[0].bind(bankXPort.getInterSlotOut());
        task[16].output[0].bind(slot[20]);
        addTask(task[16]);

        // CORRELATOR T18
        task[17] = new Correlator("CORRELATOR T18", 2, 2) {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document msg1 = inMsg1.getBody();
                String cpf = msg1.getElementsByTagName("cpf").item(0).getTextContent();

                Document msg2 = inMsg2.getBody();
                String cpf2 = msg2.getElementsByTagName("cpf").item(0).getTextContent();

                if (cpf.equals(cpf2)) {

                    // DEBUG
                    Message<Document> outMsg1 = new Message<Document>(inMsg1);
                    outMsg1.setBody(inMsg1.getBody());

                    Message<Document> outMsg2 = new Message<Document>(inMsg2);
                    outMsg2.setBody(inMsg2.getBody());

                    XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T18_X6.xml");
                    XMLHandler.writeXmlFile(outMsg2.getBody(), "debugs/T18_X9.xml");
                    // DEBUG

                    exchng.output[0].add(inMsg1);
                    exchng.output[1].add(inMsg2);

                }
            }
        };
        task[17].input[0].bind(slot[19]); // X6
        task[17].input[1].bind(slot[20]); // X9
        task[17].output[0].bind(slot[21]); // X6
        task[17].output[1].bind(slot[22]); // X9
        addTask(task[17]);

        // CONTEXT BASED CONTENT ENRICHER T19
        task[18] = new ContextBasedContentEnricher("CONTEXT BASED ENRICHER T19") {
            @SuppressWarnings("unchecked")
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                // X6
                Document x6 = inMsg1.getBody();

                // X9
                Document x9 = inMsg2.getBody();

                Document x12 = XMLHandler.newDocument();

                Element root = x12.createElement("XMLBANKX" + x9.getFirstChild().getNodeName());
                x12.appendChild(root);

                Element score = x12.createElement("score");
                score.setTextContent(x6.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x12.createElement("historic");
                historic.setTextContent(x6.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x12.createElement("name");
                name.setTextContent(x6.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x12.createElement("adress");
                adress.setTextContent(x6.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x12.createElement("cpf");
                cpf.setTextContent(x6.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x12.createElement("birth");
                birth.setTextContent(x6.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x12.createElement("income");
                income.setTextContent(x6.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x12.createElement("value");
                value.setTextContent(x6.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x12.createElement("instalment");
                instalment.setTextContent(x6.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element fiador = x12.createElement("fiador");
                fiador.setTextContent(x6.getElementsByTagName("fiador").item(0).getTextContent());
                root.appendChild(fiador);

                Element garantia = x12.createElement("garantia");
                garantia.setTextContent(x6.getElementsByTagName("garantia").item(0).getTextContent());
                root.appendChild(garantia);

                Element bank = x12.createElement("bank");
                bank.setTextContent("bankX");
                root.appendChild(bank);

                Element interestRate = x12.createElement("interestRate");
                interestRate.setTextContent(x9.getElementsByTagName("interestRate").item(0).getTextContent());
                root.appendChild(interestRate);

                Message<Document> outMsg = new Message<Document>();
                outMsg.setBody(x12);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T19_X12.xml");
                // DEBUG

                System.out.println("Correlator X Header: " + outMsg.getHeader());

                exchng.output[0].add(outMsg);

            }
        };
        task[18].input[0].bind(slot[21]);
        task[18].input[1].bind(slot[22]);
        task[18].output[0].bind(slot[23]);
        addTask(task[18]);

        // BANCO Y
        // REPLICATOR T20
        task[19] = new Replicator("REPLICATOR T20", 2);
        task[19].input[0].bind(slot[16]);
        task[19].output[0].bind(slot[24]); // VAI PARA O TRANSLATOR T21
        task[19].output[1].bind(slot[25]); // VAI PARA O CORRELATOR T23
        addTask(task[19]);

        // TRANSLATOR T21
        task[20] = new Translator("TRANSLATOR T21") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T21_X7.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[20].input[0].bind(slot[24]);
        task[20].output[0].bind(bankYPort.getInterSlotIn());
        addTask(task[20]);

        // TRANSLATOR T22
        task[21] = new Translator("TRANSLATOR T22") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T22_X10.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[21].input[0].bind(bankYPort.getInterSlotOut());
        task[21].output[0].bind(slot[26]);
        addTask(task[21]);

        // CORRELATOR T23
        task[22] = new Correlator("CORRELATOR T23", 2, 2) {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document msg1 = inMsg1.getBody();
                String cpf = msg1.getElementsByTagName("cpf").item(0).getTextContent();

                Document msg2 = inMsg2.getBody();
                String cpf2 = msg2.getElementsByTagName("cpf").item(0).getTextContent();

                if (cpf.equals(cpf2)) {

                    // DEBUG
                    Message<Document> outMsg1 = new Message<Document>(inMsg1);
                    outMsg1.setBody(inMsg1.getBody());

                    Message<Document> outMsg2 = new Message<Document>(inMsg2);
                    outMsg2.setBody(inMsg2.getBody());

                    XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T23_X7.xml");
                    XMLHandler.writeXmlFile(outMsg2.getBody(), "debugs/T23_X10.xml");
                    // DEBUG

                    exchng.output[0].add(inMsg1);
                    exchng.output[1].add(inMsg2);

                }
            }
        };
        task[22].input[0].bind(slot[25]); // X7
        task[22].input[1].bind(slot[26]); // X10
        task[22].output[0].bind(slot[27]); // X7
        task[22].output[1].bind(slot[28]); // X10
        addTask(task[22]);

        // CONTEXT BASED CONTENT ENRICHER T24
        task[23] = new ContextBasedContentEnricher("CONTEXT BASED ENRICHER T24") {
            @SuppressWarnings("unchecked")
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                // X7
                Document x7 = inMsg1.getBody();

                // X10
                Document x10 = inMsg2.getBody();

                Document x13 = XMLHandler.newDocument();

                Element root = x13.createElement("XMLBANKY" + x10.getFirstChild().getNodeName());
                x13.appendChild(root);

                Element score = x13.createElement("score");
                score.setTextContent(x7.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x13.createElement("historic");
                historic.setTextContent(x7.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x13.createElement("name");
                name.setTextContent(x7.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x13.createElement("adress");
                adress.setTextContent(x7.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x13.createElement("cpf");
                cpf.setTextContent(x7.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x13.createElement("birth");
                birth.setTextContent(x7.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x13.createElement("income");
                income.setTextContent(x7.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x13.createElement("value");
                value.setTextContent(x7.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x13.createElement("instalment");
                instalment.setTextContent(x7.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element fiador = x13.createElement("fiador");
                fiador.setTextContent(x7.getElementsByTagName("fiador").item(0).getTextContent());
                root.appendChild(fiador);

                Element garantia = x13.createElement("garantia");
                garantia.setTextContent(x7.getElementsByTagName("garantia").item(0).getTextContent());
                root.appendChild(garantia);

                Element bank = x13.createElement("bank");
                bank.setTextContent("bankY");
                root.appendChild(bank);

                Element interestRate = x13.createElement("interestRate");
                interestRate.setTextContent(x10.getElementsByTagName("interestRate").item(0).getTextContent());
                root.appendChild(interestRate);

                Message<Document> outMsg = new Message<Document>();
                outMsg.setBody(x13);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T24_X13.xml");
                // DEBUG

                System.out.println("Correlator Y Header: " + outMsg.getHeader());

                exchng.output[0].add(outMsg);

            }
        };
        task[23].input[0].bind(slot[27]);
        task[23].input[1].bind(slot[28]);
        task[23].output[0].bind(slot[29]);
        addTask(task[23]);

        // BANCO Z
        // REPLICATOR T25
        task[24] = new Replicator("REPLICATOR T25", 2);
        task[24].input[0].bind(slot[17]);
        task[24].output[0].bind(slot[30]); // VAI PARA O TRANSLATOR T26
        task[24].output[1].bind(slot[31]); // VAI PARA O CORRELATOR T28
        addTask(task[24]);

        // TRANSLATOR T26
        task[25] = new Translator("TRANSLATOR T26") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T26_X8.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[25].input[0].bind(slot[30]);
        task[25].output[0].bind(bankZPort.getInterSlotIn());
        addTask(task[25]);

        // TRANSLATOR T27
        task[26] = new Translator("TRANSLATOR T27") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                // DEBUG
                Message<Document> outMsg = new Message<Document>(inMsg);
                outMsg.setBody(inMsg.getBody());

                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T27_X11.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[26].input[0].bind(bankZPort.getInterSlotOut());
        task[26].output[0].bind(slot[32]);
        addTask(task[26]);

        // CORRELATOR T28
        task[27] = new Correlator("CORRELATOR T28", 2, 2) {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                Document msg1 = inMsg1.getBody();
                String cpf = msg1.getElementsByTagName("cpf").item(0).getTextContent();

                Document msg2 = inMsg2.getBody();
                String cpf2 = msg2.getElementsByTagName("cpf").item(0).getTextContent();

                if (cpf.equals(cpf2)) {

                    // DEBUG
                    Message<Document> outMsg1 = new Message<Document>(inMsg1);
                    outMsg1.setBody(inMsg1.getBody());

                    Message<Document> outMsg2 = new Message<Document>(inMsg2);
                    outMsg2.setBody(inMsg2.getBody());

                    XMLHandler.writeXmlFile(outMsg1.getBody(), "debugs/T28_X8.xml");
                    XMLHandler.writeXmlFile(outMsg2.getBody(), "debugs/T28_X11.xml");
                    // DEBUG

                    exchng.output[0].add(inMsg1);
                    exchng.output[1].add(inMsg2);

                }
            }
        };
        task[27].input[0].bind(slot[31]); // X8
        task[27].input[1].bind(slot[32]); // X11
        task[27].output[0].bind(slot[33]); // X8
        task[27].output[1].bind(slot[34]); // X11
        addTask(task[27]);

        // CONTEXT BASED CONTENT ENRICHER T29
        task[28] = new ContextBasedContentEnricher("CONTEXT BASED ENRICHER T29") {
            @SuppressWarnings("unchecked")
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {

                Message<Document> inMsg1 = (Message<Document>) exchng.input[0].poll();
                Message<Document> inMsg2 = (Message<Document>) exchng.input[1].poll();

                // X8
                Document x8 = inMsg1.getBody();

                // X11
                Document x11 = inMsg2.getBody();

                Document x14 = XMLHandler.newDocument();

                Element root = x14.createElement("XMLBANKZ" + x11.getFirstChild().getNodeName());
                x14.appendChild(root);

                Element score = x14.createElement("score");
                score.setTextContent(x8.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x14.createElement("historic");
                historic.setTextContent(x8.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x14.createElement("name");
                name.setTextContent(x8.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x14.createElement("adress");
                adress.setTextContent(x8.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x14.createElement("cpf");
                cpf.setTextContent(x8.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x14.createElement("birth");
                birth.setTextContent(x8.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x14.createElement("income");
                income.setTextContent(x8.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x14.createElement("value");
                value.setTextContent(x8.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x14.createElement("instalment");
                instalment.setTextContent(x8.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element bank = x14.createElement("bank");
                bank.setTextContent("bankZ");
                root.appendChild(bank);

                Element interestRate = x14.createElement("interestRate");
                interestRate.setTextContent(x11.getElementsByTagName("interestRate").item(0).getTextContent());
                root.appendChild(interestRate);

                Message<Document> outMsg = new Message<Document>();
                outMsg.setBody(x14);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T29_X14.xml");
                // DEBUG

                System.out.println("Correlator Z Header: " + outMsg.getHeader());

                exchng.output[0].add(outMsg);

            }
        };
        task[28].input[0].bind(slot[33]); // X8
        task[28].input[1].bind(slot[34]); // X11
        task[28].output[0].bind(slot[35]); // X14
        addTask(task[28]);

        // MERGER T30
        task[29] = new Merger("MERGER T30", 3, 1);
        task[29].input[0].bind(slot[23]);
        task[29].input[1].bind(slot[29]);
        task[29].input[2].bind(slot[35]);
        task[29].output[0].bind(slot[36]);
        addTask(task[29]);

        // AGGREGATOR T31
        task[30] = new Aggregator("AGGREGATOR T31", 3, 1) {
            @Override
            public void doWork(Exchange exchange) throws TaskExecutionException {

                System.out.println("Aggregator");

                HashMap<String, String> data = new HashMap<String, String>();

                Document docHere = null;

                Message<Document> outMsg = new Message<Document>();

                int count = 0;
                
                while (!exchange.input[0].isEmpty()) {

                    count++;
                    
                    System.out.println("While Aggregator");

                    Message<Document> msg = (Message<Document>) exchange.input[0].poll();

                    docHere = msg.getBody();

                    // DEBUG
                    XMLHandler.writeXmlFile(docHere, "debugs/T31_TEST" + count + ".xml");
                    // DEBUG

                    //String XML_NAME = docHere.getFirstChild().getNodeName();
                    //int numBank = XML_NAME.charAt(8);
                    //int numBanks = XML_NAME.charAt(9);
                    data.put(docHere.getElementsByTagName("bank").item(0).getTextContent(), docHere.getElementsByTagName("interestRate").item(0).getTextContent());

                }

                Document x15 = XMLHandler.newDocument();

                Element root = x15.createElement("XMLAGGREGATOR");
                x15.appendChild(root);

                Element score = x15.createElement("score");
                score.setTextContent(docHere.getElementsByTagName("score").item(0).getTextContent());
                root.appendChild(score);

                Element historic = x15.createElement("historic");
                historic.setTextContent(docHere.getElementsByTagName("historic").item(0).getTextContent());
                root.appendChild(historic);

                Element name = x15.createElement("name");
                name.setTextContent(docHere.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element adress = x15.createElement("adress");
                adress.setTextContent(docHere.getElementsByTagName("adress").item(0).getTextContent());
                root.appendChild(adress);

                Element cpf = x15.createElement("cpf");
                cpf.setTextContent(docHere.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element birth = x15.createElement("birth");
                birth.setTextContent(docHere.getElementsByTagName("birth").item(0).getTextContent());
                root.appendChild(birth);

                Element income = x15.createElement("income");
                income.setTextContent(docHere.getElementsByTagName("income").item(0).getTextContent());
                root.appendChild(income);

                Element value = x15.createElement("value");
                value.setTextContent(docHere.getElementsByTagName("value").item(0).getTextContent());
                root.appendChild(value);

                Element instalment = x15.createElement("instalment");
                instalment.setTextContent(docHere.getElementsByTagName("instalment").item(0).getTextContent());
                root.appendChild(instalment);

                Element banks = x15.createElement("banks");

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();

                    Element bank = x15.createElement("bankAndInterest");
                    bank.setTextContent(k + ": " + v);
                    banks.appendChild(bank);
                }

                outMsg.setBody(x15);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T31_X15.xml");
                // DEBUG

                exchange.output[0].add(outMsg);

            }
        };
        task[30].input[0].bind(slot[36]);
        task[30].output[0].bind(slot[37]);
        addTask(task[30]);

        // SLIMMER T32
        task[31] = new Slimmer("SLIMMER T32") {
            @Override
            public void doWork(Exchange exchng) throws TaskExecutionException {
                Message<Document> inMsg = (Message<Document>) exchng.input[0].poll();

                System.out.println("Slimmer");

                Document msgHere = inMsg.getBody();

                Document docFinal = XMLHandler.newDocument();

                Element root = docFinal.createElement("XMLFINAL");
                docFinal.appendChild(root);

                Element name = docFinal.createElement("name");
                name.setTextContent(msgHere.getElementsByTagName("name").item(0).getTextContent());
                root.appendChild(name);

                Element cpf = docFinal.createElement("cpf");
                cpf.setTextContent(msgHere.getElementsByTagName("cpf").item(0).getTextContent());
                root.appendChild(cpf);

                Element banksAndInterests = docFinal.createElement("banksAndInterests");
                root.appendChild(banksAndInterests);

                NodeList numBanks = msgHere.getElementsByTagName("bankAndInterest");

                for (int i = 0; i < numBanks.getLength(); i++) {

                    Element bk = (Element) numBanks.item(i);

                    Element bankAndInterest = docFinal.createElement("bankAndInterest");
                    bankAndInterest.setTextContent(bk.getTextContent());
                    banksAndInterests.appendChild(bankAndInterest);

                }

                Message<Document> outMsg = new Message<Document>();
                outMsg.setBody(docFinal);

                // DEBUG
                XMLHandler.writeXmlFile(outMsg.getBody(), "debugs/T32_X16.xml");
                // DEBUG

                exchng.output[0].add(outMsg);

            }
        };
        task[31].input[0].bind(slot[37]);
        task[31].output[0].bind(exitPort.getInterSlot());
        addTask(task[31]);
    }

}
