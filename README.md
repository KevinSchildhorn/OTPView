# OTPView
 Heavily customizable OTP or Pin View

The OTPView also supports `android:inputType`.

| Attribute | Format | Description | Default |
|-----------|--------|-------------|---------|
| otp_itemCount | integer | The length of the input | 1 |
| otp_showCursor | boolean | Should show cursor | false |
| otp_itemWidth | dimension | width of each item | 44dp |
| otp_itemHeight | dimension | height of each item | 44dp |
| otp_cursorColor | color | color of the cursor | Black |
| otp_allcaps | boolean | all caps(if fails set input type) | false |
| otp_marginBetween | dimension | margin between each item horizontally | 8dp |
| otp_isPassword | boolean | sets previously entered items as hidden (NOT Implemented yet) | false |
|  |  |  |  |
| otp_textSize | dimension | The text size of items past the cursor | 14dp |
| otp_textColor | integer | The text color of items past the cursor | Black |
| otp_backgroundImage | reference | The background image of items past the cursor | N/A |
| otp_Font | reference | The font of items past the cursor | N/A |
|  |  |  |  |
| otp_highlightedTextSize | dimension |  The text size of the highlighted item | otp_textSize |
| otp_highlightedTextColor | integer | The text color of the highlighted item | otp_textColor |
| otp_highlightedBackgroundImage | reference | The background image of the highlighted item | otp_backgroundImage |
| otp_highlightedFont | reference | The font of the highlighted item | otp_Font |
|  |  |  |  |
| otp_filledTextSize | dimension | The text size of items before the cursor | otp_textSize |
| otp_filledTextColor | integer | The text color of items before the cursor | otp_textColor |
| otp_filledBackgroundImage | reference | The background image of items before the cursor| otp_backgroundImage |
| otp_filledFont | reference | The font of items before the cursor | otp_Font |
