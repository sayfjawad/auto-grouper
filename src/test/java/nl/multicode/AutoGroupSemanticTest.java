package nl.multicode;

import nl.multicode.model.AutoGroupResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AutoGroupSemanticTest {

    private final AutoGroup autoGroup = new AutoGroup();

    @Test
    void groupsErrorMessagesBySharedStructure() {
        List<String> input = List.of(
                "Error 504: Gateway timeout",
                "Error 502: Gateway bad response",
                "User logged in"
        );

        final var result =
                autoGroup.autoGroupSentences(input, 5, 2);

        assertThat(result.getGroups()).hasSize(1);

        final var key = result.getGroups().keySet().iterator().next();
        assertThat(key).contains("Error");

        assertThat(result.getUngrouped())
                .containsValue("User logged in");
    }

    @Test
    void preservesPartialOverlapsNotTokenBased() {
        final var input = List.of(
                "processingPayment",
                "failedPaymentProcessing"
        );

        final var result =
                autoGroup.autoGroupSentences(input, 6, 2);

        assertThat(result.hasGroups()).isTrue();
        assertThat(result.getGroups().keySet())
                .anyMatch(k -> k.contains("Payment"));
    }
}
