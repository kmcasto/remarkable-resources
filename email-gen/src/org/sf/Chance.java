package org.sf;
 
import jdk.nashorn.internal.objects.NativeDate;
 
public interface Chance {
 
    // Basic
    public Boolean bool();
    public String character();
    public Double floating();
    public Long integer();
    public Long natural();
    public String string();
 
    // Text
    public String paragraph();
    public String sentence();
    public String syllable();
    public String word();
 
    // Person
    public Integer age();
    public NativeDate birthday();
    public String cpf(); // Brazilian Tax ID
    public String first();
    public String gender();
    public String last();
    public String name();
    public String prefix();
    public String ssn(); // Social Security Number
    public String suffix();
 
    // Mobile
    public String android_id();
    public String apple_token();
    public String bb_pin();
    public String wp7_anid();
    // Needs base64 encoder
    // public String wp8_anid2();
 
    // Web
    public String color();
    public String domain();
    public String email();
    public String fbid();
    public String google_analytics();
    public String hashtag();
    public String ip();
    public String ipv6();
    public String klout();
    public String tld();
    public String twitter();
    public String url();
 
    // Location
    public String address();
    public String altitude();
    public String areacode();
    public String city();
    public String coordinates();
    public String country();
    public String depth();
    public String geohash();
    public String latitude();
    public String longitude();
    public String phone();
    public String postal();
    public String province();
    public String state();
    public String street();
    public String zip();
 
    // Time
    public String ampm();
    public String date();
    public String hammertime();
    public String hour();
    public String millisecond();
    public String minute();
    public String month();
    public String second();
    public String timestamp();
    public String year();
 
    // Finance
    public String cc();
    public String cc_type();
    public String currency();
    public String currency_pair();
    public String dollar();
    public String exp();
    public String exp_month();
    public String exp_year();
 
    // Misc
    public String guid();
    public String hash();
    public String radio();
    public String tv();
 
}