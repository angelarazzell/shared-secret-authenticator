shared-secret-authenticator
=============================

Netbeans project for an authenticator prototype to check both users' entries for shared secret and question. Users' answers are encrypted using the Whirlpool hash algorithm.

- User 1 enters the agreed 'secret' word and asks user 2 a question to verify his/her identity. 
- This information is then requested from user 2 to fill in.
- Both secret and answer to the question are concatenated and encrypted using the whirlpool algorithm. The encrypted input from user 1 and user 2 are compared in order to verify that they match.

This project uses code downloaded from http://www.larc.usp.br/~pbarreto/WhirlpoolPage.html.

![ScreenPrint](https://raw.github.com/angelarazzell/shared-secret-authenticator/blob/master/images/screen_shot.png)


