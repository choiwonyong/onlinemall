# Online Mall
![다운로드](https://user-images.githubusercontent.com/80744275/120988984-06fcb000-c7ba-11eb-819f-6b6e67bd8f26.png)


# Online Mall (리포트)

본 프로젝트는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성하였습니다. 
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위하여 수행하였습니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

- [ OnlineMall ](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
  - [신규 개발 조직의 추가](#신규-개발-조직의-추가)

# 서비스 시나리오

기능적 요구사항
1. 고객(Customer)이 상품을 선택하여 주문한다.
1. 고객이 결제한다
1. 결제가 완료되면 주문 내역이 상품팀에게 전달된다.
1. 상품팀이 확인하여 배달을 출발한다.
1. 고객이 주문을 취소할 수 있다.
1. 주문이 취소되면 결제와 배달이 취소된다
1. 고객이 주문상태를 마이페이지에서 중간중간 조회한다

비기능적 요구사항
1. 트랜잭션
    1. 결제가 되지 않은 주문건은 아예 거래가 성립되지 않아야 한다  Sync 호출 
1. 장애격리
    1. 상품관리 기능이 수행되지 않더라도 주문은 365일 24시간 받을 수 있어야 한다  Async (event-driven), Eventual Consistency
    1. 결제시스템이 과중되면 사용자를 잠시동안 받지 않고 결제를 잠시후에 하도록 유도한다  Circuit breaker, fallback
1. 성능
    1. 고객이 주문상태 확인을 MyPage(프론트엔드)에서 확인할 수 있어야 한다  CQRS


# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684144-2a893200-826a-11ea-9a01-79927d3a0107.png)

## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/80744275/121147146-9322dc00-c87b-11eb-9ce2-ee405f6081cf.png)


## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과: http://www.msaez.io/#/storming/XyOvvgYhWFflODDKFLJZTLywC4E2/mine/739218fcb43a7278cd28719420cdd8c3


### 이벤트 도출
![image](https://user-images.githubusercontent.com/80744275/121009060-ab88ed00-c7ce-11eb-95ea-25344d32be25.png)

### 부적격 이벤트 탈락
![image](https://user-images.githubusercontent.com/80744275/121009364-0ae6fd00-c7cf-11eb-91ab-482e4b7259f3.png)

    - 과정중 도출된 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함

### 액터, 커맨드 부착하여 읽기 좋게
![image](https://user-images.githubusercontent.com/80744275/121009531-3ec22280-c7cf-11eb-8d52-92590bd83e1d.png)

### 어그리게잇으로 묶기
![image](https://user-images.githubusercontent.com/80744275/121009648-64e7c280-c7cf-11eb-9b9a-72ba851c4afc.png)

    - app의 Order, 주문의 Product, 결제의 Payment은 그와 연결된 command 와 event 들에 의하여 트랜잭션이 유지되어야 하는 단위로 그들 끼리 묶어줌

### 바운디드 컨텍스트로 묶기

![image](https://user-images.githubusercontent.com/80744275/121009938-b55f2000-c7cf-11eb-894f-f029ccc39b2a.png)

    - 도메인 서열 분리 
        - Core Domain: app(front), Product : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 app 의 경우 1주일 1회 미만, Product 의 경우 1개월 1회 미만
        - General Domain:   pay : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 (핑크색으로 이후 전환할 예정)


### 폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)

![image](https://user-images.githubusercontent.com/80744275/121010444-61087000-c7d0-11eb-90e1-e0811416098b.png)

### 1차 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증

![image](https://user-images.githubusercontent.com/80744275/121010692-afb60a00-c7d0-11eb-9b5f-a6e50c53534b.png)

    - 고객이 상품을 선택하여 주문한다 (ok)
    - 고객이 결제한다 (ok)
    - 주문이 되면 주문된 상품정보가 상품팀에게 전달된다 (ok)
    - 상품팀이 주문을 확인하여 배송팀에 전달한다 (ok)

![image](https://user-images.githubusercontent.com/80744275/121010824-d83e0400-c7d0-11eb-960e-93d489a49754.png)

    - 고객이 주문을 취소할 수 있다 (ok)
    - 주문이 취소되면 결제, 상품, 배달이 모두 취소된다. (ok)
    - 고객이 주문상태를 중간중간 조회한다 (View-green sticker 의 추가로 ok) 


### 비기능 요구사항에 대한 검증

![image](https://user-images.githubusercontent.com/80744275/121011357-77fb9200-c7d1-11eb-868f-5688a225c9fe.png)

    - 마이크로 서비스를 넘나드는 시나리오에 대한 트랜잭션 처리
        - 고객 주문시 결제처리:  결제가 완료되지 않은 주문은 절대 받지 않는다는 경영자의 오랜 신념(?) 에 따라, ACID 트랜잭션 적용. 주문와료시 결제처리에 대해서는 Request-Response 방식 처리
        - 결제 완료시 상품팀 연결 및 배송처리:  App(front) 에서 Store 마이크로서비스로 주문요청이 전달되는 과정에 있어서 Store 마이크로 서비스가 별도의 배포주기를 가지기 때문에 Eventual Consistency 방식으로 트랜잭션 처리함.
        - 나머지 모든 inter-microservice 트랜잭션: 주문상태, 배달상태 등 모든 이벤트에 대해 마이페이지 등록하는 등, 데이터 일관성의 시점이 크리티컬하지 않은 모든 경우가 대부분이라 판단, Eventual Consistency 를 기본으로 채택함.




## 헥사고날 아키텍처 다이어그램 도출
    
![image](https://user-images.githubusercontent.com/80744275/121011626-c872ef80-c7d1-11eb-97cf-04b50d2ed122.png)


    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
# gateway의 Application.yml

 - id: app
   uri: http://localhost:8081
   predicates:
 	- Path=/orders/** /myPages/**/menus/**
 - id: pay
   uri: http://localhost:8082
   predicates:
        - Path=/pays/** 
 - id: product
   uri: http://localhost:8083
   predicates:
        - Path=/products/** 
 - id: delivery
   uri: http://localhost:8084
   predicates:
        - Path=/deliveries/** 

# 서비스 실행 

cd app
mvn spring-boot:run

cd pay
mvn spring-boot:run 

cd product
mvn spring-boot:run  

cd delivery
mvn spring-boot:run 
```

## DDD 의 적용

- 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다: (예시는 Delivery 마이크로 서비스). 이때 가능한 현업에서 사용하는 언어 (유비쿼터스 랭귀지)를 그대로 사용하려고 노력했다. 하지만, 일부 구현에 있어서 영문이 아닌 경우는 실행이 불가능한 경우가 있기 때문에 영문을 사용하여 구현.

```
package onlinemall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String status;
    private String customerId;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

```
- Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리가 없도록 데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다.

```
package onlinemall;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="deliveries", path="deliveries")
public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long>{

}
```
- 적용 후 REST API 의 테스트
 - Kafka 기동(cd kafka_2.12-2.7.0\bin\windows) 
  - zookeeper : zookeeper-server-start.bat ../../config/zookeeper.properties
  - kafka : kafka-server-start.bat ../../config/server.properties --override delete.topic.enable=true
  - kafka Message : kafka-console-consumer.bat --bootstrap-server http://localhost:9092 --topic onlinemall --from-beginning
  - kafka 확인 : 
    ![image](https://user-images.githubusercontent.com/80744275/121151306-332e3480-c87f-11eb-87ba-bf60be4796ff.png)

- app서비스의 주문처리
```
http POST http://localhost:8081/orders productId=1000 productName=TV qty=1 status=ordered unitPrice=1000000 adderss=Seoul customerId=CHOI

```
![image](https://user-images.githubusercontent.com/80744275/121121182-10d5f000-c85a-11eb-835f-b72e60d918e7.png)

![image](https://user-images.githubusercontent.com/80744275/121121512-a5405280-c85a-11eb-9087-5c8a0e6efcf8.png)

- 상품 접수 완료 서비스 처리
```
http POST http://localhost:8083/products orderId=1 productId=1000 qty=1 productName=TV status=OrderAccepted

```
![image](https://user-images.githubusercontent.com/80744275/121121813-46c7a400-c85b-11eb-8a06-bd1ee44153b3.png)

![image](https://user-images.githubusercontent.com/80744275/121121880-6d85da80-c85b-11eb-9247-45738bf69860.png)

- 배달 출발 서비스 처리
```
http -f POST http://localhost:8084/deliveries/started orderId=1 status=DeliveryStarted customerId=CHOI address=Seoul

```
![image](https://user-images.githubusercontent.com/80744275/121122131-d8cfac80-c85b-11eb-880e-ee955215c6e6.png)

![image](https://user-images.githubusercontent.com/80744275/121122774-fcdfbd80-c85c-11eb-85a3-563cd0cc6557.png)


## 폴리글랏 퍼시스턴스

Delivery는 RDB 보다는 Document DB / NoSQL 계열의 데이터베이스인 Mongo DB 를 사용하기로 하였다. 이를 위해 delivery 의 선언에는 @Entity 가 아닌 @Document 로 마킹되었으며, 별다른 작업없이 기존의 Entity Pattern 과 Repository Pattern 적용과 데이터베이스 제품의 설정 (application.yml) 및 pom.xml 추가만으로 MongoDB 에 부착시켰다

```
# Delivery.java

package onlinemall;

//@Entity
//@Table(name="Delivery_table")
@Document(collection = "Delivery_table")
public class Delivery {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private Long orderId;
    private String status;
    private String customerId;
    private String address;


# DeliveryRepository.java

package onlinemall;

@RepositoryRestResource(collectionResourceRel="deliveries", path="deliveries")
//public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long>{

    public interface DeliveryRepository extends MongoRepository<Delivery, Long>{

}

# application.yml

  spring:
  #MongoDB
  data:
    mongodb:
      uri: mongodb://localhost:27017/tutorial

# pom.xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

# Delivery_table 생성 확인

http POST http://localhost:8084/deliveries orderId=1 status=DeliveryStarted customerId=CHOI address=Seoul

```
### MongoDB Table 생성 및 저장 확인

![image](https://user-images.githubusercontent.com/80744275/120778684-76c42e00-c561-11eb-9558-af2e93f46705.png)

## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 주문(app)->결제(pay) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- 결제서비스를 호출하기 위하여 Stub과 (FeignClient) 를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 

```
package onlinemall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="pay", url="http://localhost:8082")//, fallback = PayServiceFallback.class 
public interface PayService {

    @RequestMapping(method= RequestMethod.POST, path="/pays")
    public void payment(@RequestBody Pay pay);

}
```

- 주문을 받은 직후(@PostPersist) 결제를 요청하도록 처리
```
# Order.java (Entity)

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        onlinemall.external.Pay pay = new onlinemall.external.Pay();
        // mappings goes here
        
        pay.setOrderId(getId());

        if(getUnitPrice()!=null){
            
            pay.setUnitPrice(getUnitPrice());
            pay.setProductId(getProductId());
            pay.setQty(getQty());
            pay.setStatus("Product Ordered");


        AppApplication.applicationContext.getBean(onlinemall.external.PayService.class)
            .payment(pay);

        }
    }
```

- 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 결제 시스템이 장애가 나면 주문도 못받는다는 것을 확인:

```
# 결제 (pay) 서비스를 잠시 내려놓음 (ctrl+c)

# 주문처리 1
http POST http://localhost:8081/orders productId=1000 productName=TV qty=1 status=ordered unitPrice=1000000 adderss=Seoul customerId=CHOI   #Fail

```

![image](https://user-images.githubusercontent.com/80744275/121123071-8becd580-c85d-11eb-88a9-9b387d89a90b.png)

```
# 결제 (pay) 서비스를 잠시 내려놓음 (ctrl+c)

# 주문처리 2
http POST http://localhost:8081/orders productId=2000 productName=Mobile qty=1 status=ordered unitPrice=2000000 adderss=Seoul customerId=KIM   #Fail

```

![image](https://user-images.githubusercontent.com/80744275/121123177-b8085680-c85d-11eb-839e-80a6978167d1.png)

```
# 결제 (pay) 서비스 실행 (mvn spring-boot:run)
# 주문처리 1
http POST http://localhost:8081/orders productId=1000 productName=TV qty=1 status=ordered unitPrice=1000000 adderss=Seoul customerId=CHOI   #Success

```

![image](https://user-images.githubusercontent.com/80744275/121123313-fb62c500-c85d-11eb-99e5-58c00d3f65cf.png)

```
# 결제 (pay) 서비스 실행 (mvn spring-boot:run)
# 주문처리 2
http POST http://localhost:8081/orders productId=2000 productName=Mobile qty=1 status=ordered unitPrice=2000000 adderss=Seoul customerId=KIM   #Success

```

![image](https://user-images.githubusercontent.com/80744275/121123380-17666680-c85e-11eb-9b0f-05f57365c6a6.png)

- 또한 과도한 요청시에 서비스 장애가 도미노 처럼 벌어질 수 있다. (서킷브레이커, 폴백 처리는 운영단계에서 설명한다.)




## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트


결제가 이루어진 후에 상점시스템으로 이를 알려주는 행위는 동기식이 아니라 비 동기식으로 처리하여 상점 시스템의 처리를 위하여 결제주문이 블로킹 되지 않아도록 처리한다.
 
- 이를 위하여 결제이력에 기록을 남긴 후에 곧바로 결제승인이 되었다는 도메인 이벤트를 카프카로 송출한다(Publish)
 
```
package onlinemall;

@Entity
@Table(name="Pay_table")
public class Pay {

 ...
    @PrePersist
    public void onPrePersist(){

            PaymentApproved paymentApproved = new PaymentApproved();
            BeanUtils.copyProperties(this, paymentApproved);
            paymentApproved.publishAfterCommit();
 
       }     
    }
```
- 결제승인되었다는 도메인 이벤트 카프카로 송출한다.(Publish)

![image](https://user-images.githubusercontent.com/80744275/121123508-4ed51300-c85e-11eb-869e-7284f366aecd.png)

- 상품 서비스에서는 결제승인 이벤트에 대해서 이를 수신하여 자신의 정책을 처리하도록 PolicyHandler 를 구현한다:

```
package onlinemall;

...

@Service
public class PolicyHandler{
    @Autowired ProductRepository productRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_OrderPrepare(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener OrderPrepare : " + paymentApproved.toJson() + "\n\n");
         
        // 결재 완료 후 주문 정보를 받았으니 상품을 준비 합니다. 
        }       
    }

```
- 실제 구현을 하자면 시스템에서 알려 주고 상품 준비를 마친후, 주문 상태를 UI에 입력할테니, 우선 주문정보를 DB에 받아놓은 후, 이후 처리는 해당 Aggregate 내에서 하면 되겠다.:
  
```
  @Service
  public class PolicyHandler{
    @Autowired ProductRepository productRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_OrderPrepare(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener OrderPrepare : " + paymentApproved.toJson() + "\n\n");

        // Sample Logic //

        Product product = new Product();
        
        product.setOrderId(paymentApproved.getOrderId());
        product.setProductId(paymentApproved.getProductId());
        product.setQty(paymentApproved.getQty());
        product.setStatus("OrderPrepare");                 
        productRepository.save(product);
            
    }

```

- 상품 시스템은 주문/결제와 완전히 분리되어있으며, 이벤트 수신에 따라 처리되기 때문에, 상품 시스템이 유지보수로 인해 잠시 내려간 상태라도 주문을 받는데 문제가 없다.

```
# 상품팀 서비스 (Product) 를 잠시 내려놓음 (ctrl+c)

# 주문처리 1
http POST http://localhost:8081/orders productId=1000 productName=TV qty=1 status=ordered unitPrice=1000000 adderss=Seoul customerId=Back   #Success
```

![image](https://user-images.githubusercontent.com/80744275/121123699-9f4c7080-c85e-11eb-84d9-a9de587efa1f.png)

```
# 상품팀 서비스 (Product) 를 잠시 내려놓음 (ctrl+c)

# 주문처리 2
http POST http://localhost:8081/orders productId=2000 productName=Mobile qty=1 status=ordered unitPrice=2000000 adderss=Seoul customerId=LEE   #Success
```

![image](https://user-images.githubusercontent.com/80744275/121123793-c86d0100-c85e-11eb-9d0f-407ed47befa2.png)

```
# 주문상태 확인
http GET localhost:8081/myPages     # 주문상태 안바뀜 확인
```

![image](https://user-images.githubusercontent.com/80744275/121123933-036f3480-c85f-11eb-8ba5-462b1e8835e2.png)

```
#Product 서비스 기동
cd product
mvn spring-boot:run
```
- DB접속하여 OrderPrepare상태 확인

![image](https://user-images.githubusercontent.com/80744275/121126449-256ab600-c863-11eb-9e4c-ff2fa4452bda.png)


```
# 주문 접수 완료 
http POST http://localhost:8083/products orderId=11 productId=1000 qty=1 productName=TV status=OrderAccepted
http POST http://localhost:8083/products orderId=13 productId=2000 qty=1 productName=Mobile status=OrderAccepted

http GET localhost:8081/myPages     
```

- orderId=11,13 이 "OrderAccepted" 변경됨을 확인

![image](https://user-images.githubusercontent.com/80744275/121126703-914d1e80-c863-11eb-8d34-ce0fac3aad67.png)

- DB접속하여 OrderAccepted 상태 확인
![image](https://user-images.githubusercontent.com/80744275/121126850-d07b6f80-c863-11eb-8e4f-f92dfcf05b0f.png)

## CQRS(Command and Query Responsibility Segregation)

```
# 주문처리 
http POST http://localhost:8081/orders productId=1000 productName=TV qty=1 status=ordered unitPrice=1000000 adderss=Seoul customerId=Back   #Success
```

![image](https://user-images.githubusercontent.com/80744275/121125328-5cd86300-c861-11eb-8f5b-697a4d69a8a8.png)

```
# MyPage 확인 - 상품 주문, 결제 완료 
http GET localhost:8081/myPages
```

![image](https://user-images.githubusercontent.com/80744275/121125428-81ccd600-c861-11eb-89ce-f4b55224cd0d.png)

```
# 상품 접수 완료 
http POST http://localhost:8083/products orderId=15 productId=1000 qty=1 productName=TV status=OrderAccepted   #Success
```

![image](https://user-images.githubusercontent.com/80744275/121125550-b476ce80-c861-11eb-9fef-962c2ce841d4.png)

```
# MyPage 확인 - 상품접수 완료
http GET localhost:8081/myPages
```

![image](https://user-images.githubusercontent.com/80744275/121125592-c5bfdb00-c861-11eb-88ea-d93ecbea9a5a.png)

```
# 배달 시작 
http -f POST http://localhost:8084/deliveries/started orderId=15 status=DeliveryStarted customerId=Back address=Seoul   #Success
```

![image](https://user-images.githubusercontent.com/80744275/121125713-f6a01000-c861-11eb-8df8-d15c838bdaa6.png)

```
# MyPage 확인 - 배달시작
http GET localhost:8081/myPages
```

![image](https://user-images.githubusercontent.com/80744275/121125744-07e91c80-c862-11eb-90ec-a0fbc441d4af.png)

## API Gateway
API Gateway를 통하여 마이크로 서비스들 진입점을 하나로 한다.
- gateway의 application.yml에 라우팅 경로 설정

![image](https://user-images.githubusercontent.com/80744275/121280997-29a1dc80-c912-11eb-98df-2209827f79b0.png)

- EKS에 배포시 MSA는 서비스 타입을 ClusterIP(default)로 설정하여 클러스터 내부에서만 호출 가능하도록 설정

```
kubectl expose deploy order --type=ClusterIP --port=8080
kubectl expose deploy pay --type=ClusterIP --port=8080
kubectl expose deploy product --type=ClusterIP --port=8080
kubectl expose deploy delivery --type=ClusterIP --port=8080

```

- API Gateway는 서비스 타입을 LoadBalancer로 설정하여 외부 호출에 대한 라우팅 설정

```
kubectl expose deploy gateway --type=LoadBalancer --port=8080
```

# 운영

## CI/CD 설정

- 코드 형상관리: https://github.com/choiwonyong/onlinemall 하위 repository에 각각 구성
- 운영플랫폼: AWS의 EKS(Elastic Kubernetes Service)
- Docker Image 저장소: AWS의 ECR(Elastic Container Registry)

AWS설정
```
# 클러스터 생성 EKS
eksctl create cluster --name user20-eks --version 1.17 --nodegroup-name standard-workers --node-type t3.medium --nodes 4 --nodes-min 1 --nodes-max 4

# 클러스터 토큰 가져오기
aws eks --region eu-central-1 update-kubeconfig --name user20-eks

# 확인
kubectl config current-context

# ECR 로그인
docker login --username AWS -p $(aws ecr get-login-password --region eu-central-1) 879772956301.dkr.ecr.eu-central-1.amazonaws.com/
```


배포명령어(AWS)

```
# app
mvn package -Dmaven.test.skip=true
docker build -t 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-app:v1 .
aws ecr create-repository --repository-name user20-app --region eu-central-1
docker push 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-app:v1
kubectl create deploy app --image=879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-app:v1
kubectl expose deploy app --type=ClusterIP --port=8080

# pay
mvn package -Dmaven.test.skip=true
docker build -t 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-pay:v1 .
aws ecr create-repository --repository-name user20-pay --region eu-central-1
docker push 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-pay:v1
kubectl create deploy pay --image=879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-pay:v1
kubectl expose deploy pay --type=ClusterIP --port=8080

# product
mvn package -Dmaven.test.skip=true
docker build -t 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-product:v1 .
aws ecr create-repository --repository-name user20-product --region eu-central-1
docker push 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-product:v1
kubectl create deploy product --image=879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-product:v1
kubectl expose deploy product --type=ClusterIP --port=8080

# delivery
mvn package -Dmaven.test.skip=true
docker build -t 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-delivery:v1 .
aws ecr create-repository --repository-name user20-delivery --region eu-central-1
docker push 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-delivery:v1
kubectl create deploy delivery --image=879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-delivery:v1
kubectl expose deploy delivery --type=ClusterIP --port=8080

# gateway
mvn package -Dmaven.test.skip=true
docker build -t 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-gateway:v1 .
aws ecr create-repository --repository-name user20-gateway --region eu-central-1
docker push 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-gateway:v1
kubectl create deploy gateway --image=879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-gateway:v1
kubectl expose deploy gateway --type=LoadBalancer --port=8080
```
 정상 구동 
![image](https://user-images.githubusercontent.com/80744275/121297665-a3938f00-c92d-11eb-875f-55192e182dc1.png)

각 구현체들은 각자의 source repository 에 구성되었고, 사용한 CI/CD 플랫폼은 GCP를 사용하였으며, pipeline build script 는 각 프로젝트 폴더 이하에 cloudbuild.yml 에 포함되었다.


## 동기식 호출 / 서킷 브레이킹 / 장애격리

* 서킷 브레이킹 프레임워크의 선택: istio 사용하여 구현함

시나리오는 단말앱(app)-->결제(pay) 시의 연결을 RESTful Request/Response 로 연동하여 구현이 되어있고, 결제 요청이 과도할 경우 CB 를 통하여 장애격리.

- istio 설치
```
cd /home/project
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.7.1 TARGET_ARCH=x86_64 sh -
cd istio-1.7.1
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo --set hub=gcr.io/istio-release
kubectl label namespace default istio-injection=enabled 
배포다시

확인
kubectl get all -n istio-system
```
설치 확인
![image](https://user-images.githubusercontent.com/80744275/121298232-7c898d00-c92e-11eb-96ba-07a5cc176560.png)

- istio에서 서킷브레이커 설정
```
cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: order
spec:
  host: order
  trafficPolicy:
    connectionPool:
      tcp:
        maxConnections: 1           # 목적지로 가는 HTTP, TCP connection 최대 값. (Default 1024)
      http:
        http1MaxPendingRequests: 1  # 연결을 기다리는 request 수를 1개로 제한 (Default 
        maxRequestsPerConnection: 1 # keep alive 기능 disable
        maxRetries: 3               # 기다리는 동안 최대 재시도 수(Default 1024)
    outlierDetection:
      consecutiveErrors: 5          # 5xx 에러가 5번 발생하면
      interval: 1s                  # 1초마다 스캔 하여
      baseEjectionTime: 30s         # 30 초 동안 circuit breaking 처리   
      maxEjectionPercent: 100       # 100% 로 차단
EOF
```

* 부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:
- 동시사용자 100명
- 60초 동안 실시

```
siege -c100 -t60S -r10 --content-type "application/json" 'http://a3b48fcf762da4370bb8d90e343ab474-1079592167.eu-central-1.elb.amazonaws.com:8080/orders POST {"productId": "1000", "productName":"TV", "qty":"1", "status":"ordered", "unitPrice":"1000000", "adderss":"Seoul", "customerId":"CHOI"}'
```
![image](https://user-images.githubusercontent.com/80744275/121329511-3f35f700-c950-11eb-8858-a44791076961.png)

-서킷 브레이커 DestinationRule 삭제

```
kubectl delete dr --all;
```
- 서킷 브레이커 DestinationRule 삭제 후 확인
```
siege -c100 -t60S -r10 --content-type "application/json" 'http://a3b48fcf762da4370bb8d90e343ab474-1079592167.eu-central-1.elb.amazonaws.com:8080/orders POST {"productId": "1000", "productName":"TV", "qty":"1", "status":"ordered", "unitPrice":"1000000", "adderss":"Seoul", "customerId":"CHOI"}'
```
![image](https://user-images.githubusercontent.com/80744275/121329877-8cb26400-c950-11eb-9400-42c564c75a88.png)


### 오토스케일 아웃
앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 


- 결제서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:
```
kubectl autoscale deploy pay --min=1 --max=10 --cpu-percent=15
```
- CB 에서 했던 방식대로 워크로드를 2분 동안 걸어준다.
```
siege -c100 -t60S -r10 --content-type "application/json" 'http://a3b48fcf762da4370bb8d90e343ab474-1079592167.eu-central-1.elb.amazonaws.com:8080/orders POST {"productId": "1000", "productName":"TV", "qty":"1", "status":"ordered", "unitPrice":"1000000", "adderss":"Seoul", "customerId":"CHOI"}'
```
- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어둔다:
```
kubectl get deploy pay -w
```

![image](https://user-images.githubusercontent.com/80744275/121371749-c008e900-c978-11eb-8cfc-d93089f1bb5b.png)

- siege 의 로그가 100%여서 원하는 결과를 얻지 못함.  

![image](https://user-images.githubusercontent.com/80744275/121372350-2ee64200-c979-11eb-82f1-6d2accc94120.png)


## 무정지 재배포

* 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscaler 이나 CB 설정을 제거함

- seige 로 배포작업 직전에 워크로드를 모니터링 함.
```
siege -c100 -t120S -r10 --content-type "application/json" 'http://localhost:8081/orders POST {"item": "chicken"}'

** SIEGE 4.0.5
** Preparing 100 concurrent users for battle.
The server is now under siege...

HTTP/1.1 201     0.68 secs:     207 bytes ==> POST http://localhost:8081/orders
HTTP/1.1 201     0.68 secs:     207 bytes ==> POST http://localhost:8081/orders
HTTP/1.1 201     0.70 secs:     207 bytes ==> POST http://localhost:8081/orders
HTTP/1.1 201     0.70 secs:     207 bytes ==> POST http://localhost:8081/orders
:

```

- 새버전으로의 배포 시작
```
kubectl set image ...
```

- seige 의 화면으로 넘어가서 Availability 가 100% 미만으로 떨어졌는지 확인
```
Transactions:		        3078 hits
Availability:		       70.45 %
Elapsed time:		       120 secs
Data transferred:	        0.34 MB
Response time:		        5.60 secs
Transaction rate:	       17.15 trans/sec
Throughput:		        0.01 MB/sec
Concurrency:		       96.02

```
배포기간중 Availability 가 평소 100%에서 70% 대로 떨어지는 것을 확인. 원인은 쿠버네티스가 성급하게 새로 올려진 서비스를 READY 상태로 인식하여 서비스 유입을 진행한 것이기 때문. 이를 막기위해 Readiness Probe 를 설정함:

```
# deployment.yaml 의 readiness probe 의 설정:


kubectl apply -f kubernetes/deployment.yaml
```

- 동일한 시나리오로 재배포 한 후 Availability 확인:
```
Transactions:		        3078 hits
Availability:		       100 %
Elapsed time:		       120 secs
Data transferred:	        0.34 MB
Response time:		        5.60 secs
Transaction rate:	       17.15 trans/sec
Throughput:		        0.01 MB/sec
Concurrency:		       96.02

```

배포기간 동안 Availability 가 변화없기 때문에 무정지 재배포가 성공한 것으로 확인됨.

## Liveness
pod의 container가 정상적으로 기동되는지 확인하여, 비정상 상태인 경우 pod를 재기동하도록 한다.

아래의 값으로 liveness를 설정한다.

재기동 제어값 : /tmp/healthy 파일의 존재를 확인
기동 대기 시간 : 3초
재기동 횟수 : 5번까지 재시도
이때, 재기동 제어값인 /tmp/healthy파일을 강제로 지워 liveness가 pod를 비정상 상태라고 판단하도록 하였다.

```
cat <<EOF | kubectl apply -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app
  labels:
    app: app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: app
  template:
    metadata:
      labels:
        app: app
    spec:
      containers:
        - name: app
          image: 879772956301.dkr.ecr.eu-central-1.amazonaws.com/user20-app:v1
          args:
          - /bin/sh
          - -c
          - touch /tmp/healthy; sleep 10; rm -rf /tmp/healthy; sleep 600;
          ports:
            - containerPort: 8080
          livenessProbe:
            exec:
              command:
              - cat
              - /tmp/healthy
            initialDelaySeconds: 3
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5
          env:
          - name: station_nm
            valueFrom:
              secretKeyRef:
                name: app
                key: stationName
          - name: station_cd
            valueFrom:
              configMapKeyRef:
                name: app
                key: stationCode
EOF

```
![image](https://user-images.githubusercontent.com/80744275/121310006-d8a7dd80-c93d-11eb-996b-389712362cea.png)


# 신규 개발 조직의 추가

  ![image](https://user-images.githubusercontent.com/487999/79684133-1d6c4300-826a-11ea-94a2-602e61814ebf.png)


## 마케팅팀의 추가
    - KPI: 신규 고객의 유입률 증대와 기존 고객의 충성도 향상
    - 구현계획 마이크로 서비스: 기존 customer 마이크로 서비스를 인수하며, 고객에 음식 및 맛집 추천 서비스 등을 제공할 예정

## 이벤트 스토밍 
    ![image](https://user-images.githubusercontent.com/487999/79685356-2b729180-8273-11ea-9361-a434065f2249.png)


## 헥사고날 아키텍처 변화 

![image](https://user-images.githubusercontent.com/487999/79685243-1d704100-8272-11ea-8ef6-f4869c509996.png)

## 구현  

기존의 마이크로 서비스에 수정을 발생시키지 않도록 Inbund 요청을 REST 가 아닌 Event 를 Subscribe 하는 방식으로 구현. 기존 마이크로 서비스에 대하여 아키텍처나 기존 마이크로 서비스들의 데이터베이스 구조와 관계없이 추가됨. 

## 운영과 Retirement

Request/Response 방식으로 구현하지 않았기 때문에 서비스가 더이상 불필요해져도 Deployment 에서 제거되면 기존 마이크로 서비스에 어떤 영향도 주지 않음.

* [비교] 결제 (pay) 마이크로서비스의 경우 API 변화나 Retire 시에 app(주문) 마이크로 서비스의 변경을 초래함:

예) API 변화시
```
# Order.java (Entity)

    @PostPersist
    public void onPostPersist(){

        fooddelivery.external.결제이력 pay = new fooddelivery.external.결제이력();
        pay.setOrderId(getOrderId());
        
        Application.applicationContext.getBean(fooddelivery.external.결제이력Service.class)
                .결제(pay);

                --> 

        Application.applicationContext.getBean(fooddelivery.external.결제이력Service.class)
                .결제2(pay);

    }
```

예) Retire 시
```
# Order.java (Entity)

    @PostPersist
    public void onPostPersist(){

        /**
        fooddelivery.external.결제이력 pay = new fooddelivery.external.결제이력();
        pay.setOrderId(getOrderId());
        
        Application.applicationContext.getBean(fooddelivery.external.결제이력Service.class)
                .결제(pay);

        **/
    }
```
