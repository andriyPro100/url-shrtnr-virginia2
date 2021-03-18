package edu.kpi.testcourse.storage;

import edu.kpi.testcourse.entities.UrlAlias;
import edu.kpi.testcourse.logic.UrlShortenerConfig;
import edu.kpi.testcourse.serialization.JsonToolJacksonImpl;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlRepositoryFileImplTest {
  UrlShortenerConfig appConfig;
  UrlRepository urlRepository;

  @BeforeEach
  void preTest() {
    try {
      appConfig = new UrlShortenerConfig(
        Files.createTempDirectory("url-repository-file-test"));
      Files.write(appConfig.storageRoot().resolve("url-repository.json"), "{}".getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    urlRepository = new UrlRepositoryFileImpl(new JsonToolJacksonImpl(), appConfig);
  }

  @AfterEach
  void postTest() {
    try {
      Files.delete(appConfig.storageRoot().resolve("url-repository.json"));
      Files.delete(appConfig.storageRoot());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void shouldCreateAlias() {
    UrlAlias url = new UrlAlias("Test", "http://www.piterpumpkineater.com", "happy@zucchini.com");
    urlRepository.createUrlAlias(url);
    assertThat(urlRepository.findUrlAlias("Test")).isEqualTo(url);
  }

  @Test
  void serializesOneUrl() throws IOException {
    String alias = "Test";
    String email = "happy@zucchini.com";
    String destinationUrl = "http://www.piterpumpkineater1.com";
    UrlAlias url = new UrlAlias(alias, destinationUrl, email);
    urlRepository.createUrlAlias(url);
    Assertions.assertThat(
      Files.readString(appConfig.storageRoot()
        .resolve("url-repository.json"), StandardCharsets.UTF_8)
    ).contains(alias, email, destinationUrl);
  }

  @Test
  void shouldThrowError_whenAliasAlreadyExists() {
    UrlAlias urlAlias = new UrlAlias("Test", "http://www.piterpumpkineater2.com", "happy@zucchini.com");
    urlRepository.createUrlAlias(urlAlias);
    assertThrows(UrlRepository.AliasAlreadyExist.class, () -> urlRepository.createUrlAlias(urlAlias));
  }

  @Test
  void shouldFindNull_whenRepositoryIsEmpty() {
    String notExistsAlias = "Test";
    assertThat(urlRepository.findUrlAlias(notExistsAlias)).isNull();
  }
}

