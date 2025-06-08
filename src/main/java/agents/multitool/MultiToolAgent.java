package agents.multitool;

import agents.service.MultiToolAgentService;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.adk.tools.FunctionTool;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static agents.constants.MultiAgentConstant.MULTI_TOOL_AGENT_NAME;
import static agents.constants.MultiAgentConstant.USER_ID;

public class MultiToolAgent {

    // The run your agent with Dev UI, the ROOT_AGENT should be a global public static variable.
    public static BaseAgent ROOT_AGENT = initAgent();

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name(MULTI_TOOL_AGENT_NAME)
                .model("gemini-2.0-flash")
                .description("Agent to answer questions about the time and weather in a city.")
                .instruction(
                        "You are a helpful agent who can answer user questions about the time and weather"
                                + " in a city.")
                .tools(
                        FunctionTool.create(MultiToolAgentService.class, "getCurrentTime"),
                        FunctionTool.create(MultiToolAgentService.class, "getWeather"))
                .build();
    }

    public static void main(String[] args) throws Exception {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);

        Session session =
                runner
                        .sessionService()
                        .createSession(MULTI_TOOL_AGENT_NAME, USER_ID)
                        .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(USER_ID, session.id(), userMsg);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> System.out.println(event.stringifyContent()));
            }
        }
    }
}