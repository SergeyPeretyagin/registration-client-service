package com.userservice.mapper;

import com.userservice.domain.dto.UpdatePasswordRequestDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CreatorOldAndNewPassword {


    public static Stream<UpdatePasswordRequestDto> validStreamValidNewAndConfirmPasswords(){
        return getListValidOldNewAndConfirmPasswords().stream();
    }
    private static List<UpdatePasswordRequestDto> getListValidOldNewAndConfirmPasswords(){
        UpdatePasswordRequestDto dto1 = new UpdatePasswordRequestDto("GBO123456789","2!Cn444G56@","2!Cn444G56@");
        UpdatePasswordRequestDto dto2 = new UpdatePasswordRequestDto("GBR112233344","23!rrGHYerr3","23!rrGHYerr3");
        UpdatePasswordRequestDto dto3 = new UpdatePasswordRequestDto("GBN678934589","@fFh!tTj9%","@fFh!tTj9%");
        UpdatePasswordRequestDto dto4 = new UpdatePasswordRequestDto("GBS 111199999","asD$loh$123","asD$loh$123");
        return List.of(dto1,dto2,dto3,dto4);
    }

    public static Stream<UpdatePasswordRequestDto> streamWithNotValidPassportNumberAndValidNewAndConfirmPasswords(){
        return getListNotValidPassportNumberAndValidNewAndConfirmPasswords().stream();
    }
    private static List<UpdatePasswordRequestDto> getListNotValidPassportNumberAndValidNewAndConfirmPasswords(){
        UpdatePasswordRequestDto dto1 = new UpdatePasswordRequestDto("12345678901","2!Cn444G56@","2!Cn444G56@");
        UpdatePasswordRequestDto dto2 = new UpdatePasswordRequestDto("1122 3334447","23!rrGHYerr3","23!rrGHYerr3");
        UpdatePasswordRequestDto dto3 = new UpdatePasswordRequestDto("678","@fFh!tTj9%","@fFh!tTj9%");
        UpdatePasswordRequestDto dto4 = new UpdatePasswordRequestDto("sdsd3d3r22","asD$loh$123","asD$loh$123");
        return List.of(dto1,dto2,dto3,dto4);
    }

    public static Stream<UpdatePasswordRequestDto> streamNotValidNewAndConfirmPasswords(){
        return getListNotValidNewAndConfirmPasswords().stream();
    }
    private static List<UpdatePasswordRequestDto> getListNotValidNewAndConfirmPasswords(){
        UpdatePasswordRequestDto dto1 = new UpdatePasswordRequestDto("1112231231","2323232323","2323232323");
        UpdatePasswordRequestDto dto2 = new UpdatePasswordRequestDto("1166 898989","sdfsdfsdf","sdfsdfsdf");
        UpdatePasswordRequestDto dto3 = new UpdatePasswordRequestDto("2323555666","saasdWQEQWE","saasdWQEQWE");
        UpdatePasswordRequestDto dto4 = new UpdatePasswordRequestDto("3456 678789","$$##sdfsdf","$$##sdfsdf");
        return List.of(dto1,dto2,dto3,dto4);
    }

    public static Stream<UpdatePasswordRequestDto> streamNotEqualsNewAndConfirmPasswords(){
        return getListNotEqualsNewAndConfirmPasswords().stream();
    }
    private static List<UpdatePasswordRequestDto> getListNotEqualsNewAndConfirmPasswords(){
        UpdatePasswordRequestDto dto1 = new UpdatePasswordRequestDto("1112231231","!22sfsd$$","!22sfsd$$asda");
        UpdatePasswordRequestDto dto2 = new UpdatePasswordRequestDto("1166 898989","qwerQWE1231","qwrQWE12");
        UpdatePasswordRequestDto dto3 = new UpdatePasswordRequestDto("2323555666","$$@@cvbWER","$$@@cvbWER44");
        UpdatePasswordRequestDto dto4 = new UpdatePasswordRequestDto("3456 678789","123##@!ERW","123#@!ERW");
        return List.of(dto1,dto2,dto3,dto4);
    }



    public static Stream<Arguments> streamWithEqualsNewAndOldPasswords(){
        return getListWithEqualsNewAndOldPasswords().stream();
    }
    private static List<Arguments> getListWithEqualsNewAndOldPasswords(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UpdatePasswordRequestDto dto1 = new UpdatePasswordRequestDto("1234567890","2!Cn444G56@","2!Cn444G56@");
        UpdatePasswordRequestDto dto2 = new UpdatePasswordRequestDto("1122 333444","23!rrGHYerr3","23!rrGHYerr3");
        UpdatePasswordRequestDto dto3 = new UpdatePasswordRequestDto("6789345896","@fFh!tTj9%","@fFh!tTj9%");
        UpdatePasswordRequestDto dto4 = new UpdatePasswordRequestDto("1111 999999","asD$loh$123","asD$loh$123");
        UserProfile userProfile1 = UserProfile.builder().password(passwordEncoder.encode("2!Cn444G56@")).build();
        UserProfile userProfile2 = UserProfile.builder().password(passwordEncoder.encode("23!rrGHYerr3")).build();
        UserProfile userProfile3 = UserProfile.builder().password(passwordEncoder.encode("@fFh!tTj9%")).build();
        UserProfile userProfile4 = UserProfile.builder().password(passwordEncoder.encode("asD$loh$123")).build();
        return List.of(
                Arguments.of(dto1,userProfile1),
                Arguments.of(dto2,userProfile2),
                Arguments.of(dto3,userProfile3),
                Arguments.of(dto4,userProfile4));
    }

}
