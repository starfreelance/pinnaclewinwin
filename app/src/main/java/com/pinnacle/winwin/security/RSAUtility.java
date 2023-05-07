package com.pinnacle.winwin.security;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtility {

//    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgYIAzB6vWgIGr/LvF2zEEsxO8qve11FqCyIILp5EFYCeFtLeeqqjaI74DyleMyoeEQvxzyDB8m3TscAkRkXjEi9kpf205P+CktXCSPg2q/wFg3aoD4SFloGonT0GLVZnUc/dei0yz1vjn8fqOc+7mCRel6wpI33eN0Jni4/jsVz097pAp31PIz4mSz5gZ6GFMBFdSsnMT6Sj5w69hWVBX8d/9ARud8K7mcjvrYOv845E2ABNhVMqXBgvEAy0oz7v6e0TRLDR1JwEvP86dEOl+6ehnGWp7fhkgOnFzrg6kAU/VkwojZR3m6s/ACM0PnXD0LU8NsnD6fi8tKDGNovHJQIDAQAB";

//    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqzKXyf9XDRozHrPOraIxHd5bE3eaI+qcLtNwzX3uOa5ENvDUkqUtmX61pAXQ1Kp0Gc7VFV3Z3gjldWLuPReR94l24MVG77YfvHdkGPTGzKyFMxk5kruyQwSdlc1bE76nuvECVi3YHijvgjJ5nvINUKcOOFk2sLxb96KEn09JRc33Xr9n6bglAC8JHrJZ45GOtyrExXayKZjevZaVISQwmN7oL/gCBJ8loKN9AFKPOhstDzlh0GFfvS4ydoeuQF54tU5Q73aj3xShQiGUigQkcpwmaoDzmcBinbuhJFpqIlIHSfjrx4AokeoLHHz/1YKi+088DmwmQQQ5hwaFR+vFPwIDAQAB";

    /*public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDsoxkGXR7pJpkrrQuNlgsvW9Hy\n" +
            "56g29eQg1zLKJSrrBLMjbD9floj9KOuhpPe/ka0dg48Zc7VzEJ8l9cOgFEfcBMG7\n" +
            "L4GOGNKpDRplFX+XRrrx9GWYSj9eZS2t1tgXOzuaT784dj3V8eRmVIDb/HzLxZFL\n" +
            "L0qIdamXTzKbSUp5jQIDAQAB";*/
    /*public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtaebk8dVVyYKJKx9WuJ6\n" +
            "r/womG+fTJXTWnlKKidtX++czsWYjBe5rV9V9cLSwq20KtfYPIE3d8M4ZikhTHLt\n" +
            "g99uHheCXiXBTuB98EE/8vpNCns3ME2Oy88dafgeLkS95m7nJjTE+zPupsLhvvkt\n" +
            "FQke/iSLyoVawZxxjDf+r5uLgeHiyrUvS4tk1x7STfJVBdfG58FXUgxMoK33oZyE\n" +
            "tmEuRbdS/P+yMYUnSBQRy+IVTjAFFfHCo3Bddp3DEwfWeBix9RG6+9EjPl4aybIt\n" +
            "czbBrt+3w+czLM83IuVC6Qqvgt+0RMD7qWQMBq2OqnNyNQdCnJmtHFv7Dpv74GB5\n" +
            "kQIDAQAB";*/
    public static String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvV+JCcdmpO17sHC1g4XK\n" +
            "BrAc3mFE7ePgkHYcjHPfaHbaV9KZeyegm37H8YX8Dq8MDOXxJoWhe6l+Akxte66c\n" +
            "aN8xbn0EIsP4A/FHI5vllpv7nS1LR8yPBBI/lzKMG87cpKbpuOy5UBylRb3X2anB\n" +
            "2zwEImVuJC9t05ri2/4kgIi/53Mz8NePdo+NEyaZMZ2eIz4siW07N+VKIfRbyhVZ\n" +
            "qZY14KTItTdU9bCBEMewAgVRrJs9t3zPRJJ68w8CSedd8focFzsjKANmdGINMqbA\n" +
            "dAl1iBjmm9jTAqhyp/7KWCJDzg5f4UYHa+kbGKsSMRLXf3G3vQw7Vn5Qlo93VP3r\n" +
            "AFPthepjygHCf2/cVQLNZHbrFen6r8bFdWzfbzSw/t82qYwknf3yuUvL6AUoRbVB\n" +
            "dhyDQ6AQd06mi1dqsSXht8v1JTZh1a86QF/WKNAHEHYTBakgr5NFfaHt5275gn10\n" +
            "tLD0c/95xOeiYt/lWW/q3r7wQOwlIZF8lrHYjJgYHR7stQvWtqNYTgT3jRSmQupA\n" +
            "nPDF2KgaxujC5Wi1FPd8SeWo9S+UruPtnDGYkPx9PI6UjCOZlI4kz8HU/e3Mj6lv\n" +
            "riQY6FZa2A2lDMO30IFU9OpD0eeqrGz+aObJSu8tO83+fqPU3kNOpUFg/v/o8fKs\n" +
            "cXbSuYqtbGnYnP88fdTDvfcCAwEAAQ==";//4096 bit Key


//    public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBggDMHq9aAgav8u8XbMQSzE7yq97XUWoLIggunkQVgJ4W0t56qqNojvgPKV4zKh4RC/HPIMHybdOxwCRGReMSL2Sl/bTk/4KS1cJI+Dar/AWDdqgPhIWWgaidPQYtVmdRz916LTLPW+Ofx+o5z7uYJF6XrCkjfd43QmeLj+OxXPT3ukCnfU8jPiZLPmBnoYUwEV1KycxPpKPnDr2FZUFfx3/0BG53wruZyO+tg6/zjkTYAE2FUypcGC8QDLSjPu/p7RNEsNHUnAS8 /zp0Q6X7p6GcZant+GSA6cXOuDqQBT9WTCiNlHebqz8AIzQ+dcPQtTw2ycPp+Ly0oMY2i8clAgMBAAECggEAKhjPF4q15yUXdWQbUdr6FT4yP3GcuxLszHegfz7U1MR9HuAmj2seOOOYQJ1q0GjVYyclz/VO9fW7RWE2qV0YRZYAOnCwt2tSz1YKlFuSse72 /9e3I2wpXFYDMDFg66XVaeOOk6NTi9HLuOlr+qTKztarpNPCf5XvgLjmHZiFI+af +RAQ1wcBp5+Flr3QRDMFkpnRqC91XYU/E0iwWaSJfokCRM2PwgsOyi2tI/QbW+y+ +t0B7HOwlMjnIfxicAacD345mC7AKW5nBolNn7B+VkLqPKZoEwZ5fI7ErjFIJGl/ET66XyM8/tURixy18+bond145q7bjMEkCcIifL1CVQKBgQDtEfNpjzCrUAylgc/BMkC5JtcX21uCzrjaoB46S+QkyIEo0wCQ06so3bwu4doyP8fvsRoy7R1fWrDcc11y7wGdB0eor1lgqP/9gHdk7DTsPSjKtIBV1+ZHCiSp5hjzZo59ibqM7hot/Mmf0ux8A90/ufSW5fJfHlYo8Hmta/Qs/wKBgQCL2VOY9yh46pm8mtAg4n4oUd2/oxEcrhwl2bHrhFYKcbozDlGMrLLS7VB0G2FlklFiK9BhKlexWvYM9lylZ6dyFJkGkoBbKjVu0xCQBlTnsDpof4H2ZVQw3wPfFCLx9umQAN6eNU+4sZdW86Ya8R/9iUlLfHdf2Vy/l//3XIG32wKBgGP3JNAHPDuQ1rRXp7ZMrXQlh5+ctNrUtqghyn1EkSBdl/ESAHQRfWQPFBIk1HBJ6tIMuQJub+tMf7WFxAr585E2h6X+ws56lO9dY3sgCzTumSvYryEDmCuugEVQLIc+YKlZEVvp7wojRRXqyCOHMZ10mUmkDu7DCQvcU0Koed8bAoGBAIvRIeVC5bHOSxnnZcIa76p7/smDzK3x3K5y8ZDX4/XZMiGtZRbR6o87OD58yWl1WexehZ0/aiElIe5fuMydzykdMskBW1bJ4lYEqwBzdBuXcy3anSKst/mMXiB/z1g+VdDlLaza+/NZikcinaU+AY/H3AGeVPL9K99Zpk22rPIVAoGBAIQtOT8S+K/Ke3y/slCYnTe8YWB7wT7PuVoJ2hT5lUL2A+M/o/TEOeO0OWrnmqwuc9HnqcOXCdD+6258RNTPt77qNm0u6J2BFM4vPezcgw+nQ6GMBqWgbp2PLQ4PMaQ+ttsvF31l1mVJKZ1i1GyUIpLM5WRsWRJkkoZCkZMjUj/y";
//public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBggDMHq9aAgav8u8XbMQSzE7yq97XUWoLIggunkQVgJ4W0t56qqNojvgPKV4zKh4RC/HPIMHybdOxwCRGReMSL2Sl/bTk/4KS1cJI+Dar/AWDdqgPhIWWgaidPQYtVmdRz916LTLPW+Ofx+o5z7uYJF6XrCkjfd43QmeLj+OxXPT3ukCnfU8jPiZLPmBnoYUwEV1KycxPpKPnDr2FZUFfx3/0BG53wruZyO+tg6/zjkTYAE2FUypcGC8QDLSjPu/p7RNEsNHUnAS8 /zp0Q6X7p6GcZant+GSA6cXOuDqQBT9WTCiNlHebqz8AIzQ+dcPQtTw2ycPp+Ly0oMY2i8clAgMBAAECggEAKhjPF4q15yUXdWQbUdr6FT4yP3GcuxLszHegfz7U1MR9HuAmj2seOOOYQJ1q0GjVYyclz/VO9fW7RWE2qV0YRZYAOnCwt2tSz1YKlFuSse72 /9e3I2wpXFYDMDFg66XVaeOOk6NTi9HLuOlr+qTKztarpNPCf5XvgLjmHZiFI+af +RAQ1wcBp5+Flr3QRDMFkpnRqC91XYU/E0iwWaSJfokCRM2PwgsOyi2tI/QbW+y+ +t0B7HOwlMjnIfxicAacD345mC7AKW5nBolNn7B+VkLqPKZoEwZ5fI7ErjFIJGl/ET66XyM8/tURixy18+bond145q7bjMEkCcIifL1CVQKBgQDtEfNpjzCrUAylgc/BMkC5JtcX21uCzrjaoB46S+QkyIEo0wCQ06so3bwu4doyP8fvsRoy7R1fWrDcc11y7wGdB0eor1lgqP/9gHdk7DTsPSjKtIBV1+ZHCiSp5hjzZo59ibqM7hot/Mmf0ux8A90/ufSW5fJfHlYo8Hmta/Qs/wKBgQCL2VOY9yh46pm8mtAg4n4oUd2/oxEcrhwl2bHrhFYKcbozDlGMrLLS7VB0G2FlklFiK9BhKlexWvYM9lylZ6dyFJkGkoBbKjVu0xCQBlTnsDpof4H2ZVQw3wPfFCLx9umQAN6eNU+4sZdW86Ya8R/9iUlLfHdf2Vy/l//3XIG32wKBgGP3JNAHPDuQ1rRXp7ZMrXQlh5+ctNrUtqghyn1EkSBdl/ESAHQRfWQPFBIk1HBJ6tIMuQJub+tMf7WFxAr585E2h6X+ws56lO9dY3sgCzTumSvYryEDmCuugEVQLIc+YKlZEVvp7wojRRXqyCOHMZ10mUmkDu7DCQvcU0Koed8bAoGBAIvRIeVC5bHOSxnnZcIa76p7/smDzK3x3K5y8ZDX4/XZMiGtZRbR6o87OD58yWl1WexehZ0/aiElIe5fuMydzykdMskBW1bJ4lYEqwBzdBuXcy3anSKst/mMXiB/z1g+VdDlLaza+/NZikcinaU+AY/H3AGeVPL9K99Zpk22rPIVAoGBAIQtOT8S+K/Ke3y/slCYnTe8YWB7wT7PuVoJ2hT5lUL2A+M/o/TEOeO0OWrnmqwuc9HnqcOXCdD+6258RNTPt77qNm0u6J2BFM4vPezcgw+nQ6GMBqWgbp2PLQ4PMaQ+ttsvF31l1mVJKZ1i1GyUIpLM5WRsWRJkkoZCkZMjUj/y";

    public static String encrypt(String plainText, String key){
        try {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.NO_WRAP)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")),Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String data, String key) {
        try {
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key.getBytes(), Base64.DEFAULT)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.decode(data.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
