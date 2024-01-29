# HATEOAS, Self-descriptive 원칙을 따르는 REST API 구현하기

<div align="center">
<img width="329" alt="image" src="https://github.com/onetaek/devFrame/assets/86419261/6a064a1d-f6a7-40ed-b11e-8c7ef66abf34">

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fonetaek%2FdevFrame&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

</div>

## 프로젝트 소개 📌

- 이 프로젝트는 REST API에 대한 HATEOAS(hypermedia as the engine of application state) 설계 원칙과 Roy. T. Fielding이 정의한 REST API를 공부하며 구현하는 것을 목표로 합니다. 
- Spring에서 제공하는 HATEOAS 라이브러리는 있지만, 컨트롤러 단의 코드가 지저분하게 느껴져 직접 Roy. T. Fielding이 제시한 REST API 원칙을 따라 구현해보기로 결정했습니다.
- 이 프로젝트에서는 주로 HATEOAS 원칙과 self-descriptive을 중점적으로 다루며, 게시글과 댓글의 간단한 도메인을 통해 실제 구현을 진행합니다.

## 시작 가이드 📌
### Requirements
For building and running the application you need:

- jdk 17

### Command
#### 프로젝트 clone 및 Q클래스 생성을 위한 complie
``` bash
$ git clone https://github.com/onetaek/devFrame.git
$ ./gradlew complieJava
```

## Stacks 📌

### Environment

![Visual Studio Code](https://img.shields.io/badge/Intellij-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             


### Development

![Java](https://img.shields.io/badge/Java-437291?style=for-the-badge&logo=openjdk&logoColor=white)
![React](https://img.shields.io/badge/Jpa-20232A?style=for-the-badge&logoColor=61DAFB)
![Strapi](https://img.shields.io/badge/Querydsl-2F2E8B?style=for-the-badge&logoColor=white)
![Next.js](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)


## 디렉토리 구조 📌

```bash
├── domain : domain별 구현부
│    ├── article
│    │    ├── controller : 컨트롤러
│    │    ├── dto
│    │    │    ├── proxy : entity에서 영속성을 분리하기 위한 dto
│    │    │    ├── request : 프론트에서 받는 요청 정보를 담기위한 dto
│    │    │    └── response : 클라이언트에 응답하는 dto로 hateoas원칙을 따르기 위한 정보를 담고있음
│    │    ├── entity : jpa의 entity
│    │    ├── exception : 커스텀 예외를 가지는 패키지
│    │    ├── repository : spring data jpa의 repository와 querydsl을 통해 구현한 메서드를 가지는 패키지
│    │    └── service : 비즈니스 로직을 담는 패키지
│    └── comment
│         ├── controller
│         ├── dto
│         │    ├── proxy
│         │    ├── request
│         │    └── response
│         ├── entity
│         ├── exception
│         ├── repository
│         └── service
└── global
     ├── common
     │    ├── entity : auditing을 사용하는 공통 객체를 저장하는 패키지
     │    └── hateoas : Roy. T. Fielding이 말하는 REST API 설계원칙을 구현한 코드
     ├── config : 환경설정을 위한 패키지
     └── error : 공통 예외처리를 위한 클래스가 있는 패지키
```

## 주요 기능 📌

### addLink를 통해 hypermedia 정보를 추가하여 HATEOAS 원칙과 Self-descriptive 충족하는 REST API설계
- 다양한 편의성 메서드를 만들어서 쉽게 link 정보를 추가할 수 있도록 개발되었습니다.

### 단일건, Collection, 예외 응답을 공통으로 처리
- 공통 예외처리 기능도 HATEOAS원칙을 따르도록 개발하였습니다.

### 게시글, 댓글 CRUD
- 간단한 비즈니스 로직으로 게시글과 댓글이 1:N구로조 이루어져있습니다.

## 주요 코드(공통 영역) 📌
### ResponseEntity를 상속받는 CustomResponseEntity
- springframework에서 제공하는 ResponseEntity를 상속받아 만든 객체로 Body객체로 CustomResponseBody를 받도록 개발하였습니다. 
```java
public class CustomResponseEntity extends ResponseEntity<CustomResponseBody> {

    public CustomResponseEntity(HttpStatusCode status) {
        super(status);
    }

    public CustomResponseEntity(CustomResponseBody body, HttpStatusCode status) {
        super(body, status);
    }

    //...생략
}
```
### 응답 body 에 들어가는 CustomResponseBody 객체
- CustomResponseBody는 모든 응답의 기본이 되는 값으로 단일건, 컬랙션, 예외 응답은 모두 CustomResponseBoby를 상속받아서 구현되었습니다.
```java
@Getter
public class CustomResponseBody {

    private Boolean succeeded;
    private Map<String, Link> _links;

    public CustomResponseBody(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public void addLink(LinkProxy linkProxy) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = new HashMap<>(Map.of(linkProxy.getValue(), linkProxy.getLink()));
        } else {
            this._links.put(linkProxy.getValue(), linkProxy.getLink());
        }
    }
    //...생략
}
```
### 단일건 응답 객체
- BasicResponse를 content로 가지는 응답으로 BasicResponse(뒤에서 추가설명)는 DB에서 가져온 데이터에서 links정보를 추가할 수 있도록하는 객체입니다.
```java
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomSingleResponseBody extends CustomResponseBody {

    private BasicResponse content;

    public CustomSingleResponseBody(BasicResponse content) {
        super(true);
        this.content = content;
    }
}
```

### Collection 응답 객체
- 제네릭을 사용하여 BasicResponse를 상속받는 데이터만 Collection객체로 가지는 데이터만 받을 수 있도록 개발하였습니다.
- size() 를 통해 Collection객체의 size도 확인할 수 있도록 개발하였습니다.
```java
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomCollectionResponseBody<C extends Collection<? extends BasicResponse>> extends CustomResponseBody {

    private C content;
    private int size;

    public CustomCollectionResponseBody(C basicResponseCollection) {
        super(true);
        this.content = basicResponseCollection;
        this.size = basicResponseCollection.size();
    }

}
```

### 예외(Error) 응답 객체
- 예외 메시지, 상태코드, 검증 등의 데이터를 담을 수 있도록 설계하였습니다.
```java
@Getter
public class CustomErrorResponse extends CustomResponseBody {
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public CustomErrorResponse(HttpStatus httpStatus, int code, String message, Map<String, String> validation) {
        super(false);
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.validation = validation == null ? new HashMap<>() : validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
```

### BasicResponse
- DB에서 가져온 데이터에서 links 정보를 담을 수 있게 해주는 객체로 모든 응답 객체는 BasicResponse를 상속 받아 구현되야 하도록 설계 되었습니다.
```java
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse {
    private Long id;
    private Map<String, Link> _links;

    public BasicResponse() {}

    public BasicResponse(Long id) {
        this.id = id;
    }

    public BasicResponse addLink(LinkProxy linkProxy) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = new HashMap<>(Map.of(linkProxy.getValue(), linkProxy.getLink()));
        } else {
            this._links.put(linkProxy.getValue(), linkProxy.getLink());
        }
        return this;
    }

    //생략...
}
```

### LinkBuilder
- link를 만들어주는 객체로 20개 이상의 메서드를 개발하여 개발시 쉽게 link정보를 추가할 수 있도록 개발하였습니다.
- link 정보에 domain링크를 넣을지, uri만 넣을지를 yml 설정을 통해 핸들링할 수 있도록 개발하였습니다.
- 대표적인 편의성 메서드로 crud 라는 메서드를 통해 CRUD 4가지 hypermedia 정보를 넣을 수 있는 메서드도 개발하였습니다.
```java
@Getter
@Component
public class LinkBuilder {

    private static String host;
    private static Boolean showHost;

    @Value("${frame.web.host}")
    public void setHost(String hostValue) {
        host = hostValue;
    }

    @Value("${frame.web.show-host}")
    public void setShowHost(Boolean showHostValue) {
        showHost = showHostValue;
    }

    public static LinkProxy of(String value ,String uri, HttpMethod httpMethod, String mediaType) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , httpMethod, mediaType));
    }

    public static LinkProxy of(String value ,String uri, HttpMethod httpMethod, String mediaType, String description) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , httpMethod, mediaType, description));
    }

    public static LinkProxy self(String uri) {
        return LinkProxy.of("self", Link.of(handleUrl(uri) , HttpMethod.GET, MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy read(String uri) {
        return LinkProxy.of("read", Link.of(handleUrl(uri) , HttpMethod.GET,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy create(String uri) {
        return LinkProxy.of("create", Link.of(handleUrl(uri) , HttpMethod.POST,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy update(String uri) {
        return LinkProxy.of("update", Link.of(handleUrl(uri) , HttpMethod.PATCH,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy delete(String uri) {
        return LinkProxy.of("delete", Link.of(handleUrl(uri) , HttpMethod.DELETE,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy[] crud(String uri, Long id) {
        return Arrays.stream(LinkAction.values())
                .map(item -> {
                    String totalUri = LinkAction.CREATE == item ? uri : String.format("%s/%s",uri, id);
                    return LinkProxy.of(item.getValue(),Link.of(
                            showHost ? host + totalUri : totalUri, item.getHttpMethod(),
                            MediaType.APPLICATION_JSON_VALUE,
                            String.format("%s %s rest api", item.getDescription(), convertUri(uri)))
                    );
                }).toArray(LinkProxy[]::new);
    }

    //생략...
}
```

## 주요 코드(도메인 영역) 📌
### 게시글 생성 REST API Controller단 코드
- CustomResponse의 created(201), succeeded(200) 등의 다양한 메서드를 통해 상태코드를 정의할 수 있습니다.
- addLink에 uri정보를 넣으면 hypermedia를 추가할 수 있어 쉽게 REST API를 만들 수 있습니다.
```java
@PostMapping
public CustomResponseEntity create(@Validated @RequestBody ArticleCreateRequest request) {
    ArticleResponse articleResponse = ArticleResponse.fromProxy(
            articleCommandService.create(ArticleCreateRequest.toServiceRequest(request)));
    return CustomResponse.created(articleResponse)
            .addLink(LinkBuilder.self(REQUEST_URI + "/" + articleResponse.getId()));
}
```
- 응답
```application/json
{
    "succeeded": true,
    "_links": {
        "self": {
            "href": "http://localhost:8080/articles/20",
            "httpMethod": "GET",
            "mediaType": "application/json"
        }
    },
    "content": {
        "id": 20,
        "title": "HATEOAS REST API 구현",
        "content": "Roy. T. Fielding이 정의한 REST API",
        "writer": "오원택",
        "views": 0,
        "createdTime": "2024-01-29T23:57:10.2806844",
        "modifiedTime": "2024-01-29T23:57:10.2806844"
    }
}
```

### 게시글 단일 REST API Controller단 코드
- 편의성 메서드인 LinkBuilder의 crud 메서드를 사용하면 쉽게 CRUD 4가지에 대한 hypermedia 정보를 추가할 수 있습니다.
```java
@GetMapping("/{id}")
public CustomResponseEntity findById(@PathVariable Long id) {
    return CustomResponse.succeeded(
            ArticleResponse.fromProxy(
                    articleQueryService.findById(id)
            ).addLinks(LinkBuilder.crud(REQUEST_URI, id))
    );
}
```
- 응답
```application/json
{
    "succeeded": true,
    "content": {
        "id": 1,
        "_links": {
            "self": {
                "href": "http://localhost:8080/articles/1",
                "httpMethod": "GET",
                "mediaType": "application/json",
                "description": "read article rest api"
            },
            "create": {
                "href": "http://localhost:8080/articles",
                "httpMethod": "POST",
                "mediaType": "application/json",
                "description": "create article rest api"
            },
            "update": {
                "href": "http://localhost:8080/articles/1",
                "httpMethod": "PATCH",
                "mediaType": "application/json",
                "description": "update article rest api"
            },
            "delete": {
                "href": "http://localhost:8080/articles/1",
                "httpMethod": "DELETE",
                "mediaType": "application/json",
                "description": "delete article rest api"
            }
        },
        "title": "Ancient Wonders",
        "content": "Exploring the beauty of ancient ruins in Greece. The history is truly mesmerizing.",
        "writer": "HistoryExplorer1",
        "views": 2,
        "createdTime": "2024-01-30T00:23:24.948051",
        "modifiedTime": "2024-01-30T00:23:24.948051"
    }
}
```

### 게시글 목록 REST API Controller단 코드
- Collection응답으로 stream을 사용하여 각각의 데이터에 hypermedia정보(CRUD, 댓글)를 추가하였습니다.
- CustomResponse에 addLink를 하여 전체 Collection을 조회하는 self descriptive 정보도 추가할 수 있습니다.
```java
@GetMapping
public CustomResponseEntity findAll() {
    return CustomResponse.succeeded(
            articleQueryService.findAll().stream()
                    .map(ArticleResponse::fromProxy)
                    .peek(response -> {
                        LinkProxy[] crudLinks = LinkBuilder.crud(REQUEST_URI, response.getId());
                        String commentsUri = String.format("%s/%s/comments", REQUEST_URI, response.getId());
                        LinkProxy commentsLink = LinkBuilder.of("comments", commentsUri);
                        response.addLinks(commentsLink);
                        response.addLinks(crudLinks);
                    })
                    .toList()
    ).addLink(LinkBuilder.self(REQUEST_URI, "article list"));
}
```
- 응답
```application/json
{
    "succeeded": true,
    "_links": {
        "self": {
            "href": "http://localhost:8080/articles",
            "httpMethod": "GET",
            "mediaType": "application/json",
            "description": "article list"
        }
    },
    "content": [
        {
            "id": 1,
            "_links": {
                "comments": {
                    "href": "http://localhost:8080/articles/1/comments",
                    "httpMethod": "GET",
                    "mediaType": "application/json"
                },
                "self": {
                    "href": "http://localhost:8080/articles/1",
                    "httpMethod": "GET",
                    "mediaType": "application/json",
                    "description": "read article rest api"
                },
                "create": {
                    "href": "http://localhost:8080/articles",
                    "httpMethod": "POST",
                    "mediaType": "application/json",
                    "description": "create article rest api"
                },
                "update": {
                    "href": "http://localhost:8080/articles/1",
                    "httpMethod": "PATCH",
                    "mediaType": "application/json",
                    "description": "update article rest api"
                },
                "delete": {
                    "href": "http://localhost:8080/articles/1",
                    "httpMethod": "DELETE",
                    "mediaType": "application/json",
                    "description": "delete article rest api"
                }
            },
            "title": "Ancient Wonders",
            "content": "Exploring the beauty of ancient ruins in Greece. The history is truly mesmerizing.",
            "writer": "HistoryExplorer1",
            "views": 1,
            "createdTime": "2024-01-30T00:14:39.912289",
            "modifiedTime": "2024-01-30T00:14:39.912289"
        },
        {
            "id": 2,
            "_links": {
                "comments": {
                    "href": "http://localhost:8080/articles/2/comments",
                    "httpMethod": "GET",
                    "mediaType": "application/json"
                },
                "self": {
                    "href": "http://localhost:8080/articles/2",
                    "httpMethod": "GET",
                    "mediaType": "application/json",
                    "description": "read article rest api"
                },
                "create": {
                    "href": "http://localhost:8080/articles",
                    "httpMethod": "POST",
                    "mediaType": "application/json",
                    "description": "create article rest api"
                },
                "update": {
                    "href": "http://localhost:8080/articles/2",
                    "httpMethod": "PATCH",
                    "mediaType": "application/json",
                    "description": "update article rest api"
                },
                "delete": {
                    "href": "http://localhost:8080/articles/2",
                    "httpMethod": "DELETE",
                    "mediaType": "application/json",
                    "description": "delete article rest api"
                }
            },
            "title": "Journey to Machu Picchu",
            "content": "Hiking the Inca Trail to Machu Picchu. The breathtaking views were worth every step.",
            "writer": "TrailExplorer3",
            "views": 3,
            "createdTime": "2024-01-30T00:14:39.915033",
            "modifiedTime": "2024-01-30T00:14:39.915033"
        },
        {
            "id": 3,
            "_links": {
                "comments": {
                    "href": "http://localhost:8080/articles/3/comments",
                    "httpMethod": "GET",
                    "mediaType": "application/json"
                },
                "self": {
                    "href": "http://localhost:8080/articles/3",
                    "httpMethod": "GET",
                    "mediaType": "application/json",
                    "description": "read article rest api"
                },
                "create": {
                    "href": "http://localhost:8080/articles",
                    "httpMethod": "POST",
                    "mediaType": "application/json",
                    "description": "create article rest api"
                },
                "update": {
                    "href": "http://localhost:8080/articles/3",
                    "httpMethod": "PATCH",
                    "mediaType": "application/json",
                    "description": "update article rest api"
                },
                "delete": {
                    "href": "http://localhost:8080/articles/3",
                    "httpMethod": "DELETE",
                    "mediaType": "application/json",
                    "description": "delete article rest api"
                }
            },
            "title": "Chasing Aurora",
            "content": "Capturing the Northern Lights in Iceland. Nature at its most magical.",
            "writer": "NaturePhotographer4",
            "views": 5,
            "createdTime": "2024-01-30T00:14:39.915033",
            "modifiedTime": "2024-01-30T00:14:39.915033"
        },
        //생략...
    ],
    "size": 19
}
```

### 댓글 목록 REST API Controller단 코드
- 게시글에 대한 댓글 목록을 조회하는 api로 게시글과 동일한 방식으로 개발하였습니다.
```java
@GetMapping
public CustomResponseEntity findAllByArticleId(@PathVariable Long articleId) {
    return CustomResponse.succeeded(
            commentQueryService.findAllByArticleId(articleId).stream()
                    .map(CommentResponse::fromProxy)
                    .map(response -> response.addLinks(
                            LinkBuilder.crud("/articles/" + articleId + "/comments", response.getId()))
                    ).toList()
            ).addLinks(
                    LinkBuilder.self("/articles/" + articleId + "/comments"),
                    LinkBuilder.create("/articles/" + articleId + "/comments")
            );
}
```
- 응답
```application/json
{
    "succeeded": true,
    "_links": {
        "self": {
            "href": "http://localhost:8080/articles/1/comments",
            "httpMethod": "GET",
            "mediaType": "application/json"
        },
        "create": {
            "href": "http://localhost:8080/articles/1/comments",
            "httpMethod": "POST",
            "mediaType": "application/json"
        }
    },
    "content": [
        {
            "id": 1,
            "_links": {
                "self": {
                    "href": "http://localhost:8080/articles/1/comments/1",
                    "httpMethod": "GET",
                    "mediaType": "application/json",
                    "description": "read articles/1/comment rest api"
                },
                "create": {
                    "href": "http://localhost:8080/articles/1/comments",
                    "httpMethod": "POST",
                    "mediaType": "application/json",
                    "description": "create articles/1/comment rest api"
                },
                "update": {
                    "href": "http://localhost:8080/articles/1/comments/1",
                    "httpMethod": "PATCH",
                    "mediaType": "application/json",
                    "description": "update articles/1/comment rest api"
                },
                "delete": {
                    "href": "http://localhost:8080/articles/1/comments/1",
                    "httpMethod": "DELETE",
                    "mediaType": "application/json",
                    "description": "delete articles/1/comment rest api"
                }
            },
            "title": "Appreciation for Ancient Wonders",
            "content": "Fantastic article! Your experiences in Greece are truly inspiring.",
            "writer": "TravelEnthusiast1"
        },
        {
            "id": 20,
            "_links": {
                "self": {
                    "href": "http://localhost:8080/articles/1/comments/20",
                    "httpMethod": "GET",
                    "mediaType": "application/json",
                    "description": "read articles/1/comment rest api"
                },
                "create": {
                    "href": "http://localhost:8080/articles/1/comments",
                    "httpMethod": "POST",
                    "mediaType": "application/json",
                    "description": "create articles/1/comment rest api"
                },
                "update": {
                    "href": "http://localhost:8080/articles/1/comments/20",
                    "httpMethod": "PATCH",
                    "mediaType": "application/json",
                    "description": "update articles/1/comment rest api"
                },
                "delete": {
                    "href": "http://localhost:8080/articles/1/comments/20",
                    "httpMethod": "DELETE",
                    "mediaType": "application/json",
                    "description": "delete articles/1/comment rest api"
                }
            },
            "title": "Thrills of Japanese Alps Ski Adventure",
            "content": "Japanese Alps skiing adventure sounds thrilling. Any favorite slopes?",
            "writer": "SkiEnthusiast20"
        }
    ],
    "size": 2
}
```
## 개선할 점 🛠️
- BasicResponse를 상속 받아야 REST API를 구현할 수 있도록 코드를 구현하여 Spring data jpa에서 제공하는 Paging객체를 사용하기에 어려움이 있어서 직접 페이징에 대한 객체도 구현해야합니다.
- hypermedia정보 이외에 추가적으로 응답 값에 대한 명세 또는 생성, 수정시 필요한 컬럼 등에 대한 정보도 담을 수 있는 기능을 추가하여 응답 값 만 보고 API에 대한 모든 정보를 판단할 수 있도록 추가 기능이 필요합니다.
- contentType을 현재는 application/json을 주로 사용중인데 HAL, UBER, Collection+json 등의 다양한 하이퍼링크를 표현하는 방법을 정의한 명세를 활용하면 좋을 것 같습니다.

## 개선할 점 ✏️
- 
