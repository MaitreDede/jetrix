/**
 * Jetrix TetriNET Server
 * Copyright (C) 2001-2003  Emmanuel Bourg
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.jetrix.protocols;

import static net.jetrix.protocols.TetrinetProtocol.*;

import java.io.*;
import java.util.*;

import junit.framework.*;
import net.jetrix.*;
import net.jetrix.messages.channel.*;

/**
 * JUnit TestCase for the class net.jetrix.protocols.TetrinetProtocolTest
 *
 * @author Emmanuel Bourg
 * @version $Revision$, $Date$
 */
public class TetrinetProtocolTest extends TestCase
{
    private Protocol protocol;
    private Locale locale;

    public void setUp()
    {
        protocol = new TetrinetProtocol();
        locale = new Locale("fr");
    }

    public void testTranslatePline()
    {
        PlineMessage message = new PlineMessage();
        message.setSlot(1);
        message.setText("Hello Jetrix!");
        assertEquals("pline 1 Hello Jetrix!", protocol.translate(message, locale));
    }

    public void testGetMessagePline()
    {
        String raw = "pline 1 hello world!";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", PlineMessage.class, message.getClass());

        PlineMessage pline = (PlineMessage) message;
        assertEquals("slot", 1, pline.getSlot());
        assertEquals("text", "hello world!", pline.getText(Locale.ENGLISH));
    }

    public void testGetMessageCommand()
    {
        String raw = "pline 1 /move 1 2";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", CommandMessage.class, message.getClass());

        CommandMessage command = (CommandMessage) message;
        assertEquals("slot", 1, command.getSlot());
        assertEquals("command", "move", command.getCommand());
        assertEquals("text", "1 2", command.getText(Locale.ENGLISH));
        assertEquals("parameter count", 2, command.getParameterCount());
        assertEquals("1st parameter", "1", command.getParameter(0));
        assertEquals("2nd parameter", "2", command.getParameter(1));
    }

    public void testTranslatePlineAct()
    {
        PlineActMessage message = new PlineActMessage();
        message.setSlot(1);
        message.setText("feels faster");
        assertEquals("plineact 1 feels faster", protocol.translate(message, locale));
    }

    public void testGetMessagePlineAct()
    {
        String raw = "plineact 1 says hello world!";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", PlineActMessage.class, message.getClass());

        PlineActMessage plineact = (PlineActMessage) message;
        assertEquals("slot", 1, plineact.getSlot());
        assertEquals("text", "says hello world!", plineact.getText(Locale.ENGLISH));
    }

    public void testTranslateGmsg()
    {
        GmsgMessage message = new GmsgMessage();
        message.setText("<Smanux> hello world!");
        assertEquals("gmsg <Smanux> hello world!", protocol.translate(message, locale));
    }

    public void testGetMessageGmsg()
    {
        String raw = "gmsg <Smanux> Hello world!";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", GmsgMessage.class, message.getClass());

        GmsgMessage gmsg = (GmsgMessage) message;
        assertEquals("slot", 0, gmsg.getSlot());
        assertEquals("text", "<Smanux> Hello world!", gmsg.getText(Locale.ENGLISH));
    }

    public void testTranslatePlayerJoin()
    {
        JoinMessage message = new JoinMessage();
        message.setSlot(1);
        message.setName("Smanux");
        assertEquals("playerjoin 1 Smanux", protocol.translate(message, locale));
    }

    public void testGetMessagePlayerJoin()
    {
        String raw = "playerjoin 1 Smanux";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", JoinMessage.class, message.getClass());

        JoinMessage playerjoin = (JoinMessage) message;
        assertEquals("slot", 1, playerjoin.getSlot());
        assertEquals("name", "Smanux", playerjoin.getName());
    }

    public void testTranslateTeam1()
    {
        TeamMessage msg1 = new TeamMessage();
        msg1.setSlot(1);
        msg1.setName("LFJR");
        assertEquals("team 1 LFJR", protocol.translate(msg1, locale));
    }

    public void testTranslateTeam2()
    {
        TeamMessage msg2 = new TeamMessage();
        msg2.setSlot(1);
        assertEquals("team 1 ", protocol.translate(msg2, locale));
    }

    public void testGetMessageTeam1()
    {
        String raw = "team 1 LFJR";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", TeamMessage.class, message.getClass());

        TeamMessage team = (TeamMessage) message;
        assertEquals("slot", 1, team.getSlot());
        assertEquals("name", "LFJR", team.getName());
    }

    public void testGetMessageTeam2()
    {
        String raw = "team 1";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", TeamMessage.class, message.getClass());

        TeamMessage team = (TeamMessage) message;
        assertEquals("slot", 1, team.getSlot());
        assertEquals("name", null, team.getName());
    }

    public void testGetMessageTeam3()
    {
        // check the parsing of a team name containing a space character
        String raw = "team 1 L F J R";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", TeamMessage.class, message.getClass());

        TeamMessage team = (TeamMessage) message;
        assertEquals("slot", 1, team.getSlot());
        assertEquals("name", "L F J R", team.getName());
    }

    public void testGetMessageTeam4()
    {
        // check the parsing of a team name containing a leading and a trailing space character
        String raw = "team 1  LFJR ";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", TeamMessage.class, message.getClass());

        TeamMessage team = (TeamMessage) message;
        assertEquals("slot", 1, team.getSlot());
        assertEquals("name", "LFJR", team.getName());
    }

    public void testTranslatePlayerLeave()
    {
        LeaveMessage message = new LeaveMessage();
        message.setSlot(1);
        assertEquals("playerleave 1", protocol.translate(message, locale));
    }

    public void testGetMessagePlayerLeave()
    {
        String raw = "playerleave 1";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", LeaveMessage.class, message.getClass());

        LeaveMessage leave = (LeaveMessage) message;
        assertEquals("slot", 1, leave.getSlot());
    }

    public void testTranslatePlayerNum()
    {
        PlayerNumMessage message = new PlayerNumMessage(1);
        assertEquals("playernum 1", protocol.translate(message, locale));
    }

    public void testTranslateEndGame()
    {
        EndGameMessage message = new EndGameMessage();
        assertEquals("endgame", protocol.translate(message, locale));
    }

    public void testTranslatePlayerLost()
    {
        PlayerLostMessage message = new PlayerLostMessage();
        message.setSlot(1);
        assertEquals("playerlost 1", protocol.translate(message, locale));
    }

    public void testTranslateField()
    {
        FieldMessage message = new FieldMessage();
        message.setSlot(1);
        message.setField("XYZABCD");
        assertEquals("f 1 XYZABCD", protocol.translate(message, locale));
    }

    public void testGetMessageField()
    {
        String raw = "f 1 XYZABCD";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", FieldMessage.class, message.getClass());

        FieldMessage field = (FieldMessage) message;
        assertEquals("slot", 1, field.getSlot());
        assertEquals("field", "XYZABCD", field.getField());
    }

    public void testTranslateLevel()
    {
        LevelMessage message = new LevelMessage();
        message.setSlot(1);
        message.setLevel(50);
        assertEquals("lvl 1 50", protocol.translate(message, locale));
    }

    public void testGetMessageLevel()
    {
        String raw = "lvl 1 50";
        Message message = protocol.getMessage(raw);

        assertNotNull("message not parsed", message);
        assertEquals("message class", LevelMessage.class, message.getClass());

        LevelMessage level = (LevelMessage) message;
        assertEquals("slot", 1, level.getSlot());
        assertEquals("level", 50, level.getLevel());
    }

    public void testEncode()
    {
        byte[] ip = {127, 0, 0, 1};
        String nickname = "Smanux";
        String version = "1.13";

        assertEquals("80C210B3134A85CF71E46FD4C123A83D9E22A2F512769FE5", encode(nickname, version, ip, false));
    }

    public void testDecode()
    {
        String init = "80C210B3134A85CF71E46FD4C123A83D9E22A2F512769FE5";

        assertEquals("decoded string", "tetrisstart Smanux 1.13", decode(init));
    }

    public void testEncodeDecode()
    {
        byte[] ip = {(byte) 195, (byte) 139, (byte) 204, (byte) 206};

        String nickname = "Smanux";
        String version = "1.13";

        String init = encode(nickname, version, ip, false);

        assertNotNull(init);

        assertEquals("decoded", "tetrisstart Smanux 1.13", decode(init));
    }

    public void testLongMessage()
    {
        byte[] message = new byte[16 * 1024];
        try
        {
            protocol.readLine(new ByteArrayInputStream(message, 0, message.length), "Cp1252");
            fail("No exception raised on a 16K message");
        }
        catch (IOException e)
        {
            // expected
        }
    }

    public void testSlowClient()
    {
        try
        {
            protocol.readLine(new InputStream() {
                int i = 34;
                public int read() throws IOException
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    return --i;
                }
            }, "Cp1252");
            fail("No exception raised on extremely slow input");
        }
        catch (IOException e)
        {
            // expected
        }
    }
}
