package com.userservice.mapper;

import com.userservice.domain.dto.*;
import com.userservice.domain.entity.*;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class TestCreator {
    public static RequestNonClientDto getRequestNonClientDto(){
        return RequestNonClientDto.builder()
                .mobilePhone("000000000000")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("2233 444555")
                .countryOfResidence("ru")
                .build();
    }

    public static UserProfile getUserProfile(Client client, RequestNonClientDto requestNonClientDto){
        return UserProfile.builder()
                .id(UUID.randomUUID())
                .securityQuestion(requestNonClientDto.getSecurityQuestion())
                .securityAnswer(requestNonClientDto.getSecurityAnswer())
                .password(requestNonClientDto.getPassword())
                .email(requestNonClientDto.getEmail())
                .client(client)
                .build();
    }

    public static RequestClientDto getRequestClientDto(){
        return RequestClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .build();
    }


    public static Stream<RequestNonClientDto> validStreamRequestNonClientDto(){
        return getListValidRequestNonClientDto().stream();
    }

    public static Stream<RequestNonClientDto> notValidStreamRequestNonClientDto(){
        return getListNotValidRequestNonClientDto().stream();
    }

    public static Stream<RequestClientDto> notValidStreamRequestClientDto(){
        return getListNotValidRequestClientDto().stream();
    }

    public static Stream<RequestClientDto> validStreamRequestClientDto(){
        return getListValidRequestClientDto().stream();
    }

    public static Stream<Optional<Client>> streamClientWithValidPhoneNumberAndStatusIsNotRegistered(){
        return List.of(Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build())).stream();
    }

    public static Stream<Optional<Client>> streamClientWithNotValidPhoneNumberAndStatusIsNotRegistered(){
        return List.of(Optional.of(Client.builder().mobilePhone(String.valueOf(10000L+(long) (Math.random() * 899999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(10000000L+(long) (Math.random() * 999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000L+(long) (Math.random() * 8999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(1000000L+(long) (Math.random() * 899999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000L+(long) (Math.random() * 8999999L)))
                        .clientStatus(ClientStatus.NOT_REGISTERED).build())).stream();
    }
    public static Stream<Optional<Client>> streamClientWithValidPhoneNumberAndStatusActiveOrNotActive(){
        return List.of(Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.ACTIVE).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_ACTIVE).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.ACTIVE).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.NOT_ACTIVE).build()),
                Optional.of(Client.builder().mobilePhone(String.valueOf(100000000000L+(long) (Math.random() * 899999999999L)))
                        .clientStatus(ClientStatus.ACTIVE).build())).stream();
    }


    private static List<RequestNonClientDto> getListValidRequestNonClientDto(){
        RequestNonClientDto dto1 = RequestNonClientDto.builder()
                .mobilePhone("448888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("GBS223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto dto2 = RequestNonClientDto.builder()
                .mobilePhone("447777777777")
                .password("2!Cn123G56@")
                .securityQuestion("123rty123")
                .securityAnswer("123rty123")
                .email("qwerty@ya.com")
                .firstName("Maga")
                .middleName("Maga")
                .lastName("Maga")
                .passportNumber("GBR334455566")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto dto3 = RequestNonClientDto.builder()
                .mobilePhone("443456789012")
                .password("23!rrG126656/")
                .securityQuestion("MyDog")
                .securityAnswer("Sosiska")
                .email("d0g@mail.com")
                .firstName("Elena")
                .middleName("Elena")
                .lastName("Elena")
                .passportNumber("GBN 123456789")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto dto4 = RequestNonClientDto.builder()
                .mobilePhone("448765432109")
                .password("@fFhj568$")
                .securityQuestion("question")
                .securityAnswer("question")
                .email("aaaa@aaa.com")
                .firstName("Pavel")
                .middleName("Pavel")
                .lastName("Pavel")
                .passportNumber("GBS414545443")
                .countryOfResidence("UK resident")
                .build();
        return List.of(dto1,dto2,dto3,dto4);
    }

    private static List<RequestNonClientDto> getListNotValidRequestNonClientDto() {
        RequestNonClientDto notValidTelephone = RequestNonClientDto.builder()
                .mobilePhone("88888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidEmail = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("asdaas.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidPassport = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("11223334")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidPassword = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("111111111")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidSecurityQuestion = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidSecurityAnswer = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidFirstName = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("I")
                .middleName("Ivan")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK resident")
                .build();
        RequestNonClientDto notValidLastName = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("Ivan")
                .lastName("I")
                .passportNumber("223344455")
                .countryOfResidence("UK non-resident")
                .build();
        RequestNonClientDto notValidMiddleName = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("I")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK non-resident")
                .build();
        RequestNonClientDto notValidCountryOfResidence = RequestNonClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("qwerty123")
                .securityAnswer("qwerty123")
                .email("123@ya.com")
                .firstName("Ivan")
                .middleName("I")
                .lastName("Ivan")
                .passportNumber("223344455")
                .countryOfResidence("UK non-resident")
                .build();
        return List.of(notValidTelephone,notValidEmail,notValidPassport,notValidPassword
                ,notValidSecurityQuestion,notValidSecurityAnswer,notValidFirstName
                ,notValidLastName,notValidMiddleName,notValidCountryOfResidence);
    }

    private static List<RequestClientDto> getListNotValidRequestClientDto(){
        RequestClientDto notValidTelephone = RequestClientDto.builder()
                .mobilePhone("88888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .build();
        RequestClientDto notValidEmail = RequestClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123ya.com")
                .build();
        RequestClientDto notValidPassword = RequestClientDto.builder()
                .mobilePhone("888888888888")
                .password("123")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .build();
        RequestClientDto notValidSecurityQuestion = RequestClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("do")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .build();
        RequestClientDto notValidSecurityAnswer = RequestClientDto.builder()
                .mobilePhone("888888888888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("12")
                .email("123@ya.com")
                .build();
        return List.of(notValidTelephone,notValidEmail,notValidPassword,notValidSecurityQuestion,notValidSecurityAnswer);
    }

    private static List<RequestClientDto> getListValidRequestClientDto(){
        RequestClientDto dto1 = RequestClientDto.builder()
                .mobilePhone("448888888888")
                .password("1!Aa123456@")
                .securityQuestion("dogdog")
                .securityAnswer("dogdog")
                .email("123@ya.com")
                .build();
        RequestClientDto dto2 = RequestClientDto.builder()
                .mobilePhone("443456789012")
                .password("23!33Fg/3456@")
                .securityQuestion("qwerty")
                .securityAnswer("qwerty")
                .email("qwerty@mail.com")
                .build();
        RequestClientDto dto3 = RequestClientDto.builder()
                .mobilePhone("441110090807")
                .password("1!ABBbb*456")
                .securityQuestion("catcat")
                .securityAnswer("catcat")
                .email("cat@gmail.com")
                .build();
        RequestClientDto dto4 = RequestClientDto.builder()
                .mobilePhone("442233445566")
                .password("wer322!@mMd33")
                .securityQuestion("question")
                .securityAnswer("answ er")
                .email("123@ya.com")
                .build();
        return List.of(dto1,dto2,dto3,dto4);
    }

    public static Client getNewNotClient(RequestNonClientDto requestNonClientDto) {
        PassportData passportData = PassportData.builder()
                .id(UUID.randomUUID())
                .passportNumber(requestNonClientDto.getPassportNumber())
                .build();
        UserProfile userProfile = UserProfile.builder()
                .id(UUID.randomUUID())
                .securityQuestion(requestNonClientDto.getSecurityQuestion())
                .securityAnswer(requestNonClientDto.getSecurityAnswer())
                .password(requestNonClientDto.getPassword())
                .email(requestNonClientDto.getEmail())
//                .roles(Set.of( new Role(UUID.randomUUID(),EnumRole.ROLE_USER)))
                .build();
        return Client.builder()
                .id(UUID.randomUUID())
                .firstName(requestNonClientDto.getFirstName())
                .middleName(requestNonClientDto.getMiddleName())
                .lastName(requestNonClientDto.getLastName())
                .passportData(passportData)
                .userProfile(userProfile)
                .mobilePhone(requestNonClientDto.getMobilePhone())
                .countryOfResidence(requestNonClientDto.getCountryOfResidence())
                .build();
    }

    public static Client getNewClient(RequestClientDto requestClientDto) {
        UserProfile userProfile = UserProfile.builder()
                .securityQuestion(requestClientDto.getSecurityQuestion())
                .securityAnswer(requestClientDto.getSecurityAnswer())
                .password(requestClientDto.getPassword())
                .email(requestClientDto.getEmail())
                .build();
        return Client.builder()
                .userProfile(userProfile)
                .mobilePhone(requestClientDto.getMobilePhone())
                .build();
    }


    public static Optional<Client> getOptionalClientNotRegistered(String mobilePhone,UUID clientId) {
        return Optional.of(Client.builder().id(clientId).clientStatus(ClientStatus.NOT_REGISTERED).build());
    }

    public static Optional<Client> getOptionalClientNotFound(String mobilePhone, UUID clientId) {
        return Optional.empty();
    }

    public static Stream<Optional<Client>> streamWithClientStatusActiveAndNotActive(){
        return Stream.of(Optional.of(Client.builder().clientStatus(ClientStatus.ACTIVE).build()),
                Optional.of(Client.builder().clientStatus(ClientStatus.NOT_ACTIVE).build()));
    }

    public static NotificationDto getNotificationDto(){
        return NotificationDto.builder()
                .email(null)
                .smsNotification(true)
                .pushNotification(true)
                .emailSubscription(true)
                .build();
    }
    public static UpdatePasswordDto getUpdatePasswordDto(){
        return new UpdatePasswordDto("12345qweQWE","qweQWE123","qweQWE123");
    }

    public static Stream<UpdatePasswordDto> getStreamFromValidUpdatePasswordDto() {
        return getListFromValidUpdatePasswordDto().stream();
    }
    private static List<UpdatePasswordDto> getListFromValidUpdatePasswordDto(){
        return List.of(
                new UpdatePasswordDto("123qweQWE","qwe123QWE","qwe123QWE"),
                new UpdatePasswordDto("zxc0897!@","cvgg5fgQf34","cvgg5fgQf34"),
                new UpdatePasswordDto("!@@ffds1321", "$dollar$100", "$dollar$100")
        );
    }


    public static Stream<UpdatePasswordDto> getStreamFromNotValidUpdatePasswordDto() {
        return getListFromNotValidUpdatePasswordDto().stream();
    }
    private static List<UpdatePasswordDto> getListFromNotValidUpdatePasswordDto(){
        return List.of(
                new UpdatePasswordDto("123qwe","qwe123QWE","qwe123QWE"),
                new UpdatePasswordDto("zx","cvgg5fgQf34","cvgg5fgQf34"),
                new UpdatePasswordDto("!@@ffds1321", "123123213", "$dollar$100")
        );
    }

    public static Stream<UpdateEmailDto> getStreamFromUpdateEmailDto() {
        return getListFromUpdateEmailDto().stream();
    }
    private static List<UpdateEmailDto> getListFromUpdateEmailDto(){
        return List.of(
                new UpdateEmailDto("dog@ya.com"),
                new UpdateEmailDto( "cat@mail.com"),
                new UpdateEmailDto("mouse@gmail.com" )
        );
    }

    public static Stream<UpdateEmailDto> getStreamFromNotValidUpdateEmailDto() {
        return getListFromNotValidUpdateEmailDto().stream();
    }
    private static List<UpdateEmailDto> getListFromNotValidUpdateEmailDto(){
        return List.of(
                new UpdateEmailDto("dog@yacom"),
                new UpdateEmailDto("catmail.com"),
                new UpdateEmailDto("mouse@gmail")
        );
    }

    public static Stream<UpdateEmailDto> getStreamFromUpdateEmailDtoWithNotEqualsNewAndConfirmEmail() {
        return getListFromUpdateEmailDtoWithNotEqualsNewAndConfirmEmail().stream();
    }
    private static List<UpdateEmailDto> getListFromUpdateEmailDtoWithNotEqualsNewAndConfirmEmail(){
        return List.of(
                new UpdateEmailDto("dogf@ya.com"),
                new UpdateEmailDto("cat@mail.com"),
                new UpdateEmailDto("mouse@gmail.com")
        );
    }

    public static Stream<UpdateQADto> getStreamFromValidUpdateQADto() {
        return getListFromUpdateValidUpdateQADto().stream();
    }
    private static List<UpdateQADto> getListFromUpdateValidUpdateQADto(){
        return List.of(
                new UpdateQADto("Name of dog", "Sosiska"),
                new UpdateQADto("Name of cat", "baton"),
                new UpdateQADto("how much will be 2x3","6six6")
        );
    }

    public static Stream<UpdateQADto> getStreamFromNotValidUpdateQADto() {
        return getListFromNotValidUpdateQADto().stream();
    }
    private static List<UpdateQADto> getListFromNotValidUpdateQADto(){
        return List.of(
                new UpdateQADto("AAA", "BB"),
                new UpdateQADto("12", "cho"),
                new UpdateQADto("1x2x","6six6")
        );
    }

    public static Stream<PassportNumberRequestDto> getStreamFromValidPassportNumberRequestDto() {
        return List.of(new PassportNumberRequestDto("GBO112233445"),
                new PassportNumberRequestDto("GBR123456789"),
                new PassportNumberRequestDto("GBN 342123422")).stream();
    }


    public static Stream<PassportNumberRequestDto> getStreamFromNotValidPassportNumberRequestDto() {
        return List.of(new PassportNumberRequestDto("1122 3334"),
                new PassportNumberRequestDto(""),
                new PassportNumberRequestDto("asda12321s"),
                new PassportNumberRequestDto("asdqweghjj")).stream();
    }

    public static Stream<UserVerificationDto> getStreamFromValidUserVerificationDto() {
        return List.of(new UserVerificationDto("443456789012", "123456"),
                new UserVerificationDto("448765432109","654321"),
                new UserVerificationDto("441222333444","908070")).stream();
    }

    public static Stream<UserVerificationDto> getStreamFromNotValidUserVerificationDto() {
        return List.of(new UserVerificationDto("12345678901", "123456"),
                new UserVerificationDto("098765432109","65432123"),
                new UserVerificationDto("111222333444","sdfsdf"),
                new UserVerificationDto("1231","111111")).stream();
    }

    public static Stream<PhoneNumberDto> getStreamFromValidPhoneNumberDto() {
        return List.of(new PhoneNumberDto("896576876112"),
                new PhoneNumberDto("123456789012"),
                new PhoneNumberDto("987654321101")).stream();
    }

    public static Stream<PhoneNumberDto> getStreamFromNotValidPhoneNumberDto() {
        return List.of(new PhoneNumberDto("896576876112123"),
                new PhoneNumberDto("1234567"),
                new PhoneNumberDto("98765sadsada"),
                new PhoneNumberDto("asdfghjkl,mn")).stream();
    }


    public static String getMobilePhone() {
        return "448978888899";
    }

    public static String getverificationCode() {
        return "123456";
    }

    public static UserVerificationDto getUserVerificationDto() {
        return new UserVerificationDto("449922213377","123456");
    }

    public static PhoneNumberDto getPhoneNumberDto() {
        return new PhoneNumberDto("789786876542");
    }

    public static String getPassport() {
        return "GBO123456788";
    }

    public static String getEmail() {
        return "new@gamil.com";
    }

    public static Verification getVerification() {
        return Verification.builder()
                .receiver("899988877712")
                .verificationCode("123456")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(15, ChronoUnit.MINUTES))))
                .blockExpiration(null)
                .countRequest(0)
                .type("phone")
                .build();
    }
    public static Verification getVerificationWithTimeExpired() {
        return Verification.builder()
                .receiver("899988877712")
                .verificationCode("123456")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(15, ChronoUnit.MINUTES))))
                .blockExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(15, ChronoUnit.MINUTES))))
                .countRequest(0)
                .type("phone")
                .build();
    }
    public static Verification getVerificationWithNotValidVerificationCode() {
        return Verification.builder()
                .receiver("899988877712")
                .verificationCode("122346")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(15, ChronoUnit.MINUTES))))
                .blockExpiration(null)
                .countRequest(0)
                .type("phone")
                .build();
    }

    public static Stream<Arguments> getStreamFromValidArgumentsForChangePassword() {
        return getListFromValidArguments().stream();
    }
    private static List<Arguments> getListFromValidArguments(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserProfile profile1 = UserProfile.builder().password(passwordEncoder.encode("123qweQWE")).build();
        UserProfile profile2 = UserProfile.builder().password(passwordEncoder.encode("zxc0897!@")).build();
        UserProfile profile3 = UserProfile.builder().password(passwordEncoder.encode("!@@ffds1321")).build();

        UpdatePasswordDto passwordDto1 = new UpdatePasswordDto("123qweQWE","qwe123QWE","qwe123QWE");
        UpdatePasswordDto passwordDto2 =new UpdatePasswordDto("zxc0897!@","cvgg5fgQf34","cvgg5fgQf34");
        UpdatePasswordDto passwordDto3 = new UpdatePasswordDto("!@@ffds1321", "$dollar$100", "$dollar$100");
        return List.of(
                Arguments.of(passwordDto1,profile1),
                Arguments.of(passwordDto2,profile2),
                Arguments.of(passwordDto3,profile3));
    }

    public static Stream<UpdatePasswordDto> getStreamFromUpdatePasswordDtoWithNotEqualsNewAndConfirmPassword() {
        return getListFromUpdatePasswordDtoWithNotEqualsNewAndConfirmPassword().stream();
    }
    private static List<UpdatePasswordDto> getListFromUpdatePasswordDtoWithNotEqualsNewAndConfirmPassword(){
        return List.of(
                new UpdatePasswordDto("123qweQWE","qwe123QWE","qwe123VBE"),
                new UpdatePasswordDto("zxc0897!@","cvgg5fgQf34","cvg!5fgQf34"),
                new UpdatePasswordDto("!@@ffds1321", "$dollar$100", "$docVr$100")
        );
    }


    public static ClientInformationDto getClientInformationDto() {
        return ClientInformationDto.builder()
                .countryOfResidence("uk")
                .email("email@ya.com")
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .mobilePhone("123456789012")
                .passportNumber("1234555666")
                .build();
    }

    public static RequestClientNonRegisteredDto getRequestClientNonRegistered() {
        return new RequestClientNonRegisteredDto("441234567890","Dimon",
                "Dimon","Dimon","GBO123456789","UK resident");
    }


    public static Stream<RequestClientNonRegisteredDto> getRequestClientNonRegisteredWithNotValidData() {
        return getListRequestClientNonRegisteredWithNotValidData().stream();
    }
    private static List<RequestClientNonRegisteredDto> getListRequestClientNonRegisteredWithNotValidData(){
        return List.of(
           new RequestClientNonRegisteredDto("4412345678903","Dimon",
                "Dimon","Dimon","GBO123456789","UK resident"),
                new RequestClientNonRegisteredDto("441234567890","Dimon",
                        "Dimon","Dimon","GBO1234567894","UK resident"),
                new RequestClientNonRegisteredDto("sdfsdf","Dimon",
                        "Dimon","Dimon","GBO123456789","UK resident"),
                new RequestClientNonRegisteredDto("441234567890","Dimon",
                        "Dimon","Dimon","GBZ123456789","UK resident")
        );
    }

    public static Client getClient(RequestClientNonRegisteredDto nonRegisteredDto) {
        return Client.builder()
                .mobilePhone(nonRegisteredDto.getMobilePhone())
                .firstName(nonRegisteredDto.getFirstName())
                .lastName(nonRegisteredDto.getLastName())
                .middleName(nonRegisteredDto.getMiddleName())
                .countryOfResidence(nonRegisteredDto.getCountryOfResidence())
                .build();
    }
}
