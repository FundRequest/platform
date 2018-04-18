package io.fundrequest.platform.profile.telegram.bot;

import io.fundrequest.platform.profile.telegram.service.TelegramVerificationService;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.objects.ChatMember;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class FundRequestVerifierBot extends AbilityBot {

    private static final String NEGATIV = "We're sorry, ";
    private static final String POSITIVE = "Nice, ";
    private final TelegramVerificationService telegramVerificationService;
    private final String fundrequestChannel;
    private String fundrequestChannelLink;
    private String registrationPage;

    public FundRequestVerifierBot(final String botToken,
                                  final String botUsername,
                                  final TelegramVerificationService telegramVerificationService,
                                  final String fundrequestChannel,
                                  final String fundrequestChannelLink,
                                  final String registrationPage
                                 ) {
        super(botToken, botUsername);
        this.telegramVerificationService = telegramVerificationService;
        this.fundrequestChannel = fundrequestChannel;
        this.fundrequestChannelLink = fundrequestChannelLink;
        this.registrationPage = registrationPage;
    }

    @Override
    public int creatorId() {
        return 302351791;
    }

    public Ability verify() {
        return Ability.builder()
                      .name("verify")
                      .info("verifies your connection with FundRequest")
                      .locality(ALL)
                      .privacy(PUBLIC)
                      .action(ctx -> {
                          final GetChatMember getChatMember = new GetChatMember();
                          getChatMember.setChatId(fundrequestChannel);
                          getChatMember.setUserId(ctx.user().id());
                          try {
                              final ChatMember execute = this.execute(getChatMember);
                              if (execute.getStatus().equalsIgnoreCase("member") || execute.getStatus().equalsIgnoreCase("creator") || execute.getStatus()
                                                                                                                                              .equalsIgnoreCase("administrator")) {
                                  if (telegramVerificationService.exists(ctx.user().username())) {
                                      if (ctx.arguments().length > 0) {
                                          if (telegramVerificationService.verify(ctx.user().username(), ctx.firstArg())) {
                                              silent.send(POSITIVE + ctx.user().username() + "! We've successfully verified you as member of our community!", ctx.chatId());
                                          } else {
                                              silent.send(NEGATIV
                                                          + ctx.user().username()
                                                          + "! We found you as a member of our community, but your platform key appears to be wrong!\nex. /verify myPlatformKey",
                                                          ctx.chatId());
                                          }

                                      } else {
                                          silent.send(NEGATIV
                                                      + ctx.user().username()
                                                      + "! We found you as a member of our community, but you did not add your platform key. Please register at "
                                                      + registrationPage
                                                      + " and add your platform key to your verification.\nex. /verify myPlatformKey", ctx.chatId());
                                      }
                                  } else {
                                      silent.send(NEGATIV
                                                  + ctx.user().username()
                                                  + "! We found you as a member of our community but we failed to verify you as a registered user on our platform. Please "
                                                  + "register at "
                                                  + registrationPage
                                                  + " and include your platform key.\nex. /verify myPlatformKey", ctx.chatId());
                                  }
                              } else {
                                  silent.send(NEGATIV
                                              + ctx.user().username()
                                              + "! You were not found as a member of the fundrequest channel ("
                                              + execute.getStatus()
                                              + "). Please join us at "
                                              + fundrequestChannelLink, ctx.chatId());
                              }
                          } catch (final Exception ex) {
                              ex.printStackTrace();
                              silent.send(NEGATIV
                                          + ctx.user().username()
                                          + "! You were not found as a member of the fundrequest channel. Please join us at "
                                          + fundrequestChannelLink, ctx.chatId());
                          }
                      })
                      .build();
    }
}
