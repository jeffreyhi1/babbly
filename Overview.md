# Introduction #

Babbly uses sip in order to accomplish some sort of communication(either voip or im). It can(or should be able to) communicate with either another instance of itself or any other sip compliant client.


# What works #

Let's put it the short way: NOT MUCH. Basically you can log into your previously created sip account.
However here is a list of the currently working features:
  * User data input with expression check validation on the data
  * User authentication and authorization to a registrar(respectively proxy/registrar)
  * The Digest algorithm for HTTP Authentication (the Basic algorithm is not used since it exposes the user credentials to attackers)
  * Initialization of the SIP Stack and exposing its interfaces in a wrapper-similar fashion with the help of the SipManager class.
  * Logging of system messages by using log4j(DEBUG,INFO, etc) again in a wrapper similar fashion. (Look at the AppLog class)
  * Basic STUN functionality. Although this feature isn't enabled yet(tests pending!)
  * A threads-aware graphical Swing user interface written with the MVC design paradigm in mind. It is important to note that it still lacks on many important features and is to be enhanced as the project grows.



**A quick memorizer of the SIP requests and responses available in SIP as a mechanism of a typical handshake between two users or between a user-proxy/registrar counterparts according to RFC 3621:**
  * Possible requests are:
    * REGISTER
    * OPTIONS
    * INVITE
    * CANCEL
    * BYE
    * ACK
  * Possible responses are:
    * Provisional (1xx): Request received and being processed (e.g 180 Ringing).
    * Success (2xx): The action was successfully received, understood, and accepted (e.g. 200 OK).
    * Redirection (3xx): Further action needs to be taken (typically by sender) to complete the request(e.g 301 Moved Permanently).
    * Client Error (4xx): The request contains bad syntax or cannot be fulfilled at the server (e.g. 401 Unauthorized).
    * Server Error (5xx): The server failed to fulfill an apparently valid request.
    * Global Failure (6xx): The request cannot be fulfilled at any server.


# What is to be implemented #

  * A user should be able to stay online after successful authentication in a proxy/registrar.
  * A nice feature would be the possibility to implement the so-called PRESENCE feature (other people can see when you are online and vice versa)
  * Instant Messaging or the ability to send and receive messages(follow the RFCs!)
  * The ability to connect to another participant/client when not having a direct internet connection(e.g passive/private IP).
  * The ability to establish a connection even when all ports on the firewall protecting the private network where the current client resides are closed and access to the internet is possible solely through HTTP Proxy
  * Clear separation of the core functionality of the application from its frontend
  * Extending the graphical user interface to offer more and more features as well as to be more pleasing and user friendly, whereby not having to sacrifice a drop of usability!
  * more, more and guess what - even more!


---

# _NOTE_ #
## **_Some of the mentioned wannabe features are already added to the issues/feature requests list. However please feel free to fire issues/bugs as well as feature requests additionally at any time!_** ##