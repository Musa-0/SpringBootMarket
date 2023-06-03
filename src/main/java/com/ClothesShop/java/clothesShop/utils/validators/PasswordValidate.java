package com.ClothesShop.java.clothesShop.utils.validators;

import edu.vt.middleware.password.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordValidate {

    PasswordValidator validator;

    @PostConstruct
    private void init(){

    // длина пароля от 8 до 100
    LengthRule lengthRule = new LengthRule(8, 100);

    // нельзя пробелы
    WhitespaceRule whitespaceRule = new WhitespaceRule();

    // прававила для символов в пароле
    CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();

    // как минимум 1 цифра
    charRule.getRules().add(new DigitCharacterRule(1));

    // требуется как минимум 1 неалфавитно-цифровой символ
    charRule.getRules().add(new NonAlphanumericCharacterRule(1));

    // как минимум 1 символ верхнего регистра
    charRule.getRules().add(new UppercaseCharacterRule(1));

    // как минимум 1 символ нижнего регистра
    charRule.getRules().add(new LowercaseCharacterRule(1));

    //требуется соблюдение как минимум 3-х предыдущих правил
    charRule.setNumberOfCharacteristics(3);

    //не разрешать алфавитные последовательности
    AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();

    //содержит ли пароль цифровую последовательность
    NumericalSequenceRule numSeqRule = new NumericalSequenceRule();

    // не разрешать последовательности qwerty
    QwertySequenceRule qwertySeqRule = new QwertySequenceRule();

    // не допускать повторения 4 символов
    RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);

    // сгруппируйте все правила вместе в список
    List<Rule> ruleList = new ArrayList<Rule>();
    ruleList.add(lengthRule);
    ruleList.add(whitespaceRule);
    ruleList.add(charRule);
    ruleList.add(alphaSeqRule);
    ruleList.add(numSeqRule);
    ruleList.add(qwertySeqRule);
    ruleList.add(repeatRule);

    this.validator = new PasswordValidator(ruleList);  //передадим эти правила нашему валидатору
    }

    public List<String> is_valid(String password){
        PasswordData passwordData = new PasswordData(new Password(password));

        RuleResult result = this.validator.validate(passwordData);

        if (result.isValid()) {
            return null;
        } else {

            return validator.getMessages(result);

        }
    }


}
