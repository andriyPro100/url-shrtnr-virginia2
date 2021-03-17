package edu.kpi.testcourse.storage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import edu.kpi.testcourse.entities.UrlAlias;
import edu.kpi.testcourse.storage.UrlRepository.PermissionDenied;
import org.junit.jupiter.api.Test;

class UrlRepositoryFakeImplTest {

  @Test
  void shouldCreateAlias() {
    //GIVEN
    UrlRepository repo = new UrlRepositoryFakeImpl();

    //WHEN
    UrlAlias alias = new UrlAlias("http://r.com/short", "http://g.com/long", "aaa@bbb.com");
    repo.createUrlAlias(alias);

    //THEN
    assertThat(repo.findUrlAlias("http://r.com/short")).isEqualTo(alias);
  }

  @Test
  void shouldNotAllowToCreateSameAliases() {
    //GIVEN
    UrlRepository repo = new UrlRepositoryFakeImpl();

    //WHEN
    UrlAlias alias1 = new UrlAlias("http://r.com/short", "http://g.com/long1", "aaa@bbb.com");
    repo.createUrlAlias(alias1);

    //THEN
    UrlAlias alias2 = new UrlAlias("http://r.com/short", "http://g.com/long2", "aaa@bbb.com");
    assertThatThrownBy(() -> {
      repo.createUrlAlias(alias2);
    }).isInstanceOf(UrlRepository.AliasAlreadyExist.class);
  }

  @Test
  void shouldGetAllAliases() {
    //GIVEN
    UrlRepository repo = new UrlRepositoryFakeImpl();

    //WHEN
    UrlAlias testAlias = new UrlAlias("test", "http://localhost:8080/", "artem@gmail.com");
    UrlAlias testAlias2 = new UrlAlias("lol", "http://localhost:8080/", "artem@gmail.com");
    UrlAlias testAlias3 = new UrlAlias("shorten", "http://localhost:8080/", "lol@gmail.com");

    repo.createUrlAlias(testAlias);
    repo.createUrlAlias(testAlias2);

    //THEN
    assertFalse(repo.getAllAliases(testAlias.email()).isEmpty());
    assertThrows(PermissionDenied.class, ()->{repo.getAllAliases(testAlias3.email());});
  }

}
