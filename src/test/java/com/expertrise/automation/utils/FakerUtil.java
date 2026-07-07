package com.expertrise.automation.utils;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * FakerUtil - Utility class for generating random/fake test data
 * Dependency: com.github.javafaker:javafaker (add to pom.xml)
 *
 * <dependency>
 *   <groupId>com.github.javafaker</groupId>
 *   <artifactId>javafaker</artifactId>
 *   <version>1.0.2</version>
 * </dependency>
 */
public class FakerUtil {

    private static final Logger logger = Logger.getLogger(FakerUtil.class.getName());
    private final Faker faker;

    public FakerUtil() {
        this.faker = new Faker(new Locale("en-US"));
    }

    /**
     * S1 - Generate a random valid US address string
     *
     * @return Full address as a single string
     */
    public String getRandomValidAddress() {
        String address = faker.address().streetAddress() + ", "
                + faker.address().city() + ", "
                + faker.address().stateAbbr() + " "
                + faker.address().zipCode();
        logger.info("Generated random address: " + address);
        return address;
    }

    /**
     * S2 - Generate a full random demographic profile as a Map
     *
     * @return Map with keys: FIRSTNAME, LASTNAME, MIDDLENAME, PREFIX, SUFFIX,
     *         SSN, DOB, ADDRESS1, ADDRESS2, CITY, STATE, ZIP, COUNTRYCODE,
     *         WORKNUMBER, CONTACTNUMBER, EMAIL, GENDER, MARITALSTATUS
     */
    public Map<String, String> getRandomDemographicData() {
        Map<String, String> data = new HashMap<>();

        String[] genders = {"Male", "Female", "Non-Binary"};
        String[] maritalStatuses = {"Single", "Married", "Divorced", "Widowed"};
        String[] suffixes = {"Jr.", "Sr.", "II", "III", "IV", ""};

        data.put("FIRSTNAME", faker.name().firstName());
        data.put("LASTNAME", faker.name().lastName());
        data.put("MIDDLENAME", faker.name().firstName());
        data.put("PREFIX", faker.name().prefix());
        data.put("SUFFIX", suffixes[faker.random().nextInt(suffixes.length)]);
        data.put("SSN", faker.idNumber().ssnValid());
        data.put("DOB", faker.date().birthday(18, 70).toString());
        data.put("ADDRESS1", faker.address().streetAddress());
        data.put("ADDRESS2", faker.address().secondaryAddress());
        data.put("CITY", faker.address().city());
        data.put("STATE", faker.address().stateAbbr());
        data.put("ZIP", faker.address().zipCode());
        data.put("COUNTRYCODE", "US");
        data.put("WORKNUMBER", faker.phoneNumber().phoneNumber());
        data.put("CONTACTNUMBER", faker.phoneNumber().cellPhone());
        data.put("EMAIL", faker.internet().emailAddress());
        data.put("GENDER", genders[faker.random().nextInt(genders.length)]);
        data.put("MARITALSTATUS", maritalStatuses[faker.random().nextInt(maritalStatuses.length)]);

        logger.info("Generated random demographic data for: " + data.get("FIRSTNAME") + " " + data.get("LASTNAME"));
        return data;
    }

    /**
     * S3 (NEW) - Generate a random email address with a custom domain
     *
     * @param domain Domain to use (e.g., "gmail.com", "expertrise.com")
     * @return Random email string (e.g., john.doe123@expertrise.com)
     */
    public String getRandomEmail(String domain) {
        String localPart = faker.name().firstName().toLowerCase()
                + "." + faker.name().lastName().toLowerCase()
                + faker.number().digits(3);
        String email = localPart + "@" + domain;
        logger.info("Generated random email: " + email);
        return email;
    }

    /**
     * S4 (NEW) - Generate a random phone number based on country code
     *
     * @param countryCode Country code string (e.g., "US", "IN", "UK")
     * @return Formatted phone number string
     */
    public String getRandomPhoneNumber(String countryCode) {
        Faker localFaker;
        switch (countryCode.toUpperCase()) {
            case "IN":
                localFaker = new Faker(new Locale("en-IND"));
                break;
            case "UK":
            case "GB":
                localFaker = new Faker(new Locale("en-GB"));
                break;
            default:
                localFaker = new Faker(new Locale("en-US"));
                break;
        }
        String phone = localFaker.phoneNumber().phoneNumber();
        logger.info("Generated phone [" + countryCode + "]: " + phone);
        return phone;
    }

    /**
     * S5 (NEW) - Generate random credit card data
     *
     * @return Map with keys: CARDNUMBER, CARDHOLDERNAME, EXPIRYDATE, CVV, CARDTYPE
     */
    public Map<String, String> getRandomCreditCardData() {
        Map<String, String> card = new HashMap<>();

        String[] cardTypes = {"Visa", "MasterCard", "Amex", "Discover"};
        String cardType = cardTypes[faker.random().nextInt(cardTypes.length)];

        card.put("CARDTYPE", cardType);
        card.put("CARDHOLDERNAME", faker.name().fullName());
        card.put("CVV", String.valueOf(faker.number().numberBetween(100, 999)));
        card.put("EXPIRYDATE", String.format("%02d/%02d",
                faker.number().numberBetween(1, 12),
                faker.number().numberBetween(26, 30)));

        // Generate card number based on type
        switch (cardType) {
            case "Visa":
                card.put("CARDNUMBER", "4" + faker.number().digits(15));
                break;
            case "MasterCard":
                card.put("CARDNUMBER", "5" + faker.number().digits(15));
                break;
            case "Amex":
                card.put("CARDNUMBER", "37" + faker.number().digits(13));
                card.put("CVV", String.valueOf(faker.number().numberBetween(1000, 9999))); // Amex uses 4-digit CVV
                break;
            default:
                card.put("CARDNUMBER", "6011" + faker.number().digits(12));
                break;
        }

        logger.info("Generated random credit card: " + cardType);
        return card;
    }

    /**
     * S6 (NEW) - Generate random company/business data
     *
     * @return Map with keys: COMPANYNAME, EIN, INDUSTRY, PHONE, WEBSITE, ADDRESS
     */
    public Map<String, String> getRandomCompanyData() {
        Map<String, String> company = new HashMap<>();

        String[] industries = {"Technology", "Healthcare", "Finance", "Retail",
                "Education", "Manufacturing", "Logistics", "Media"};

        company.put("COMPANYNAME", faker.company().name());
        company.put("EIN", faker.number().digits(2) + "-" + faker.number().digits(7)); // Format: XX-XXXXXXX
        company.put("INDUSTRY", industries[faker.random().nextInt(industries.length)]);
        company.put("PHONE", faker.phoneNumber().phoneNumber());
        company.put("WEBSITE", "www." + faker.internet().domainName());
        company.put("ADDRESS", faker.address().fullAddress());

        logger.info("Generated company data: " + company.get("COMPANYNAME"));
        return company;
    }
}