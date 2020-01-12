
package acme.components;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.format.Formatter;

import acme.datatypes.Phone;
import acme.framework.helpers.MessageHelper;
import acme.framework.helpers.StringHelper;

public class PhoneFormatter implements Formatter<Phone> {

	@Override
	public String print(final Phone object, final Locale locale) {
		assert object != null;
		assert locale != null;

		String result;
		String countryCodeText, areaCodeText, numberText;

		countryCodeText = String.format("+%d", object.getCountryCode());
		areaCodeText = object.getAreaCode() == null ? " " : String.format(" (%d) ", object.getAreaCode());
		numberText = String.format("%s", object.getNumber());

		result = String.format("+%d%s%ld", countryCodeText, areaCodeText, numberText);

		return result;
	}

	@Override
	public Phone parse(final String text, final Locale locale) throws ParseException {
		assert !StringHelper.isBlank(text);
		assert locale != null;

		Phone result;
		String countryCodeRegexp, areaCodeRegexp, numberRegexp, phoneRegexp;
		Pattern pattern;
		Matcher matcher;
		String errorMessage;
		String countryCodeText;
		int countryCode;
		String areaCode, number;

		countryCodeRegexp = "\\+\\d{1,3}";
		areaCodeRegexp = "\\d{1,6}";
		numberRegexp = "\\d{1,9}([\\s-]\\d{1,9}){0,5}";
		phoneRegexp = String.format("\\s*(?<CC>%1$s)(\\s+\\((?<AC>%2$s)\\)\\s+)(?<N>%3$s)\\s*$", countryCodeRegexp, areaCodeRegexp, numberRegexp);

		pattern = Pattern.compile(phoneRegexp, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		matcher = pattern.matcher(text);

		if (!matcher.find()) {
			errorMessage = MessageHelper.getMessage("default.error.conversion", null, "Invalid value", locale);
			throw new ParseException(errorMessage, 0);
		} else {
			countryCodeText = matcher.group("CC");
			countryCode = Integer.valueOf(countryCodeText);
			areaCode = matcher.group("AC");
			number = matcher.group("N");

			result = new Phone();
			result.setCountryCode(countryCode);
			result.setAreaCode(areaCode);
			result.setNumber(number);
		}

		return result;
	}
}
