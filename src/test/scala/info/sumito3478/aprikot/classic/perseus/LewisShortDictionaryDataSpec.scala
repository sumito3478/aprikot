/* Copyright (C) 2013 sumito3478 <sumito3478@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.sumito3478.aprikot.classic.perseus

import org.scalatest.FunSpec

class LewisShortDictionaryDataSpec extends FunSpec {
  describe("LewisShortDictionaryData#html") {
    it("should return converted html text") {
      val tei = """
<entryFree id="n52" type="main" key="abdo" opt="n">
  <orth extent="full" lang="la" opt="n">ab-do</orth>
  ,
  <itype opt="n">ĭdi, ĭtum, 3</itype>
  ,
  <pos opt="n">v. a.</pos>
  <etym opt="n">2. do</etym>
  .
  <sense id="n52.0" n="I" level="1" opt="n">
    <usg type="style" opt="n">Lit.</usg>
    ,
    <hi rend="ital">to put away, remove</hi>
    : and abdere se,
    <hi rend="ital">to go away, betake one's self</hi>
    to some place:
    <cit>
      <quote lang="la">ex conspectu eri sui se abdiderunt,</quote>
      <bibl n="Perseus:abo:phi,0119,016:4:7:5" default="NO" valid="yes">
        <author>Plaut.</author>
        Ps. 4, 7, 5
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">
        pedestres copias paulum ab eo loco abditas in locis superioribus constituunt,
      </quote>
      <trans opt="n">
        <tr opt="n">removed, withdrawn</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0448,001:7:79:2" default="NO" valid="yes">
        <author>Caes.</author>
        B. G. 7, 79, 2
      </bibl>
    </cit>
    ; so with
    <hi rend="ital">ab</hi>
    :
    <cit>
      <quote lang="la">ascensu abdito a conspectu,</quote>
      <bibl n="Perseus:abo:phi,0914,001:10:14:14" default="NO" valid="yes">
        <author>Liv.</author>
        10, 14, 14
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">procul ardentes hinc precor abde faces,</quote>
      <trans opt="n">
        <tr opt="n">remove</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0660,001:2:1:82" default="NO">
        <author>Tib.</author>
        2, 1, 82
      </bibl>
    </cit>
    .—The
    <hi rend="ital">terminus ad quem</hi>
    is usually expressed by
    <hi rend="ital">in</hi>
    with
    <hi rend="ital">acc.</hi>
    :
    <cit>
      <quote lang="la">
        abdidit se in intimam Macedoniam quo potuit longissime a castris,
      </quote>
      <bibl n="Perseus:abo:phi,0474,056:13:29:4" default="NO" valid="yes">
        <author>Cic.</author>
        Fam. 13, 29, 4
      </bibl>
    </cit>
    ; so,
    <cit>
      <quote lang="la">se in contrariam partem terrarum,</quote>
      <bibl n="Perseus:abo:phi,0474,014:41:89" default="NO" valid="yes">
        <author>id.</author>
        Mur. 41, 89
      </bibl>
    </cit>
    : se in classem, Dolab. ap.
    <bibl n="Perseus:abo:phi,0474,056:9:9:2" default="NO" valid="yes">
      <author>Cic.</author>
      Fam. 9, 9, 2
    </bibl>
    :
    <cit>
      <quote lang="la">se in Menapios,</quote>
      <trans opt="n">
        <tr opt="n">to depart</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0448,001:6:5:5" default="NO" valid="yes">
        <author>Caes.</author>
        B. G. 6, 5, 5
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">In silvam Arduennam,</quote>
      <bibl default="NO">
        <author>id.</author>
        ib. 5, 3, 4
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">exercitum in interiora,</quote>
      <trans opt="n">
        <tr opt="n">to uithdraw</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,1044,001:2:110:3" default="NO">
        <author>Vell.</author>
        2, 110, 3
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">
        ea in insulam Seriphon abdita est (=ex humanā societate quasi expulsa),
      </quote>
      <trans opt="n">
        <tr opt="n">banished, exiled</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,1351,005:2:85" default="NO" valid="yes">
        <author>Tac.</author>
        A. 2, 85
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">se in bibliothecam,</quote>
      <trans opt="n">
        i. e.
        <tr opt="n">to retire to</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0474,056:7:28" default="NO" valid="yes">
        <author>Cic.</author>
        Fam. 7, 28
      </bibl>
    </cit>
    ; cf.:
    <cit>
      <quote lang="la">se totum in litteras,</quote>
      <bibl default="NO">
        <author>id.</author>
        ib. 7, 33, 2
      </bibl>
    </cit>
    .—Rarely with other
    <hi rend="ital">prepositions</hi>
    or with
    <hi rend="ital">local adv.</hi>
    : Audisne haec, Amphiaraë, sub terram abdite? Poët. (Att.?) ap.
    <bibl n="Perseus:abo:phi,0474,049:2:25:60" default="NO">
      <author>Cic.</author>
      Tusc. 2, 25, 60
    </bibl>
    ; so with
    <hi rend="ital">sub</hi>
    ,
    <bibl n="Perseus:abo:phi,0550,001:4:419" default="NO" valid="yes">
      <author>Lucr.</author>
      4, 419
    </bibl>
    :
    <cit>
      <quote lang="la">se rus,</quote>
      <bibl n="Perseus:abo:phi,0134,005:1:2:99" default="NO" valid="yes">
        <author>Ter.</author>
        Hec. 1, 2, 99
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">se domum,</quote>
      <bibl n="Perseus:abo:phi,0474,027:38:92" default="NO" valid="yes">
        <author>Cic.</author>
        Pis. 38, 92
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">se Arpinum,</quote>
      <bibl n="Perseus:abo:phi,0474,057:9:6:1" default="NO" valid="yes">
        <author>id.</author>
        Att. 9, 6, 1
      </bibl>
    </cit>
    .
  </sense>
  <sense id="n52.1" n="II" level="1" opt="n">
    <usg type="style" opt="n">Transf.</usg>
    ,
    <hi rend="ital">to hide, conceal, keep secret</hi>
    , etc. (syn.: occulto, recondo); constr.
    <hi rend="ital">aliquid</hi>
    , without or with
    <hi rend="ital">in</hi>
    and
    <case opt="n">abl.</case>
    , with other
    <hi rend="ital">prepositions</hi>
    , with
    <case opt="n">abl.</case>
    only, or
    <hi rend="ital">dat</hi>
    ., with a
    <hi rend="ital">localadv</hi>
    .
  </sense>
  <sense id="n52.2" n="(a)" level="5" opt="n">
    <hi rend="ital">Aliquid</hi>
    :
    <cit>
      <quote lang="la">
        quae partes corporis ... aspectum essent deformem habiturae, eas contexit atque abdidit (natura),
      </quote>
      <bibl n="Perseus:abo:phi,0474,055:1:35:126" default="NO">
        <author>Cic.</author>
        Off. 1, 35, 126
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">amici tabellas,</quote>
      <bibl n="Perseus:abo:phi,0474,027:17:39" default="NO" valid="yes">
        <author>id.</author>
        Pis. 17, 39
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">lacrimas, operire luctum,</quote>
      <bibl n="Perseus:abo:phi,1318,001:3:16:6" default="NO" valid="yes">
        <author>Plin.</author>
        Ep. 3, 16, 6
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">abduntur (delphini) occultanturque incognito more,</quote>
      <bibl 
      n="Perseus:abo:phi,0978,001:H. N. 9:8:7:section=22" default="NO" valid="yes">
        <author>Plin.</author>
        H. N. 9, 8, 7, § 22
</bibl>
    </cit>
    ; cf.:
    <cit>
      <quote lang="la">occultare et abdere pavorem,</quote>
      <bibl n="Perseus:abo:phi,1351,004:1:88" default="NO" valid="yes">
        <author>Tac.</author>
        H. 1, 88
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">
        pugnare cupiebant, sed retro revocanda et abdenda cupiditas erat,
      </quote>
      <bibl n="Perseus:abo:phi,0914,001:2:45:7" default="NO" valid="yes">
        <author>Liv.</author>
        2, 45, 7
      </bibl>
    </cit>
    ; so,
    <cit>
      <quote lang="la">sensus suos penitus,</quote>
      <bibl n="Perseus:abo:phi,1351,005:1:11" default="NO" valid="yes">
        <author>Tac.</author>
        A. 1, 11
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">aliquid dissimulata offensione,</quote>
      <bibl default="NO">
        <author>id.</author>
        ib. 3, 64
      </bibl>
    </cit>
    . —
  </sense>
  <sense id="n52.3" n="(b)" level="5" opt="n">
    With
    <hi rend="ital">in</hi>
    and
    <case opt="n">abl.</case>
    :
    <cit>
      <quote lang="la">cum se ille fugiens in scalarum tenebris abdidisset,</quote>
      <bibl n="Perseus:abo:phi,0474,031:15:40" default="NO" valid="yes">
        <author>Cic.</author>
        Mil. 15, 40
      </bibl>
    </cit>
    ; cf.:
    <cit>
      <quote lang="la">
        qui dispersos homines in agris et in tectis silvestribus abditos ... compulit unum in locum,
      </quote>
      <bibl default="NO">
        <author>id.</author>
        Inr. 1, 2, 2
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">abditi in tabernaculis,</quote>
      <bibl n="Perseus:abo:phi,0448,001:1:39:4" default="NO" valid="yes">
        <author>Caes.</author>
        B. G. 1, 39, 4
      </bibl>
    </cit>
    ; cf.:
    <cit>
      <quote lang="la">in silvis,</quote>
      <bibl default="NO">
        <author>id.</author>
        ib. 9, 19, 6
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">penitus qui in ferrost abditus aër,</quote>
      <bibl n="Perseus:abo:phi,0550,001:6:1037 al" default="NO" valid="yes">
        <author>Lucr.</author>
        6, 1037 al.
      </bibl>
    </cit>
    —
  </sense>
  <sense id="n52.4" n="(g)" level="5" opt="n">
    With other
    <hi rend="ital">prepp.</hi>
    :
    <cit>
      <quote lang="la">cultrum, quem sub veste abditum habebat,</quote>
      <bibl n="Perseus:abo:phi,0914,001:1:58" default="NO" valid="yes">
        <author>Liv.</author>
        1, 58
        <hi rend="ital">fin.</hi>
      </bibl>
    </cit>
    ; cf.
    <bibl n="Perseus:abo:phi,0959,006:10:715" default="NO" valid="yes">
      <author>Ov.</author>
      M. 10, 715
    </bibl>
    :
    <cit>
      <quote lang="la">ferrum carvo tenus hamo,</quote>
      <bibl n="Perseus:abo:phi,0959,010:4:719" default="NO">
        <author>id.</author>
        ib. 4, 719
      </bibl>
    </cit>
    .—（
    <foreign lang="greek">o)</foreign>
    ) With
    <case opt="n">abl.</case>
    :
    <cit>
      <quote lang="la">caput cristatā casside,</quote>
      <bibl n="Perseus:abo:phi,0959,006:8:25" default="NO" valid="yes">
        <author>Ov.</author>
        M. 8, 25
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">corpus corneā domo,</quote>
      <bibl n="Perseus:abo:phi,0975,001:2:6:5" default="NO" valid="yes">
        <author>Phaedr.</author>
        2, 6, 5
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">gladium sinu,</quote>
      <bibl n="Perseus:abo:phi,1351,005:5:7" default="NO" valid="yes">
        <author>Tac.</author>
        A. 5, 7
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">latet abditus agro,</quote>
      <bibl default="NO">
        <author>Hor.</author>
        Ep. 1, 1, 5
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">hunc (equum) abde domo,</quote>
      <bibl n="Perseus:abo:phi,0690,002:3:96" default="NO" valid="yes">
        <author>Verg.</author>
        G. 3, 96
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">ita se litteris abdiderunt, at, etc.,</quote>
      <bibl n="Perseus:abo:phi,0474,016:6:12" default="NO" valid="yes">
        <author>Cic.</author>
        Arch. 6, 12
      </bibl>
    </cit>
    ; v. Halm ad h. l.—（
    <foreign lang="greek">e</foreign>
    ) With
    <hi rend="ital">dat</hi>
    . (
    <usg type="style" opt="n">poet.</usg>
    ):
    <cit>
      <quote lang="la">lateri capulo tenus abdidit ensem,</quote>
      <trans opt="n">
        <tr opt="n">he baried</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0690,003:2:553" default="NO" valid="yes">
        <author>Verg.</author>
        A. 2, 553
      </bibl>
    </cit>
    .—（
    <foreign lang="greek">z</foreign>
    ) With
    <hi rend="ital">local adv.</hi>
    :
    <cit>
      <quote lang="la">corpus humi,</quote>
      <bibl n="Perseus:abo:phi,1242,001:4:12:38" default="NO">
        <author>Flor.</author>
        4, 12, 38
      </bibl>
    </cit>
    .—Hence.
    <pb n="6"/>
    <cb n="ABDU"/>
    <orth extent="full" lang="la" opt="n">abditus</orth>
    ,
    <itype opt="n">a, um</itype>
    ,
    <hi rend="ital">P. a., hidden, concealed, secreted, secret</hi>
    (syn.: reconditus, abscontlitus, occultus, retrusus): sub terram abditi, Att. ap.
    <bibl n="Perseus:abo:phi,0474,049:2:25:60" default="NO">
      <author>Cic.</author>
      Tusc. 2, 25, 60
    </bibl>
    :
    <cit>
      <quote lang="la">vis abdita quaedum,</quote>
      <bibl n="Perseus:abo:phi,0550,001:5:1233" default="NO" valid="yes">
        <author>Lucr.</author>
        5, 1233
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">res occultae et penitus abditae,</quote>
      <bibl n="Perseus:abo:phi,0474,050:1:19" default="NO">
        <author>Cic.</author>
        N. D. 1, 19
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">
        sunt innumerabiles de his rebus libri neque abditi neque obscuri,
      </quote>
      <bibl n="Perseus:abo:phi,0474,037:2:20:84" default="NO" valid="yes">
        <author>id.</author>
        de Or. 2, 20, 84
      </bibl>
    </cit>
    : haec esse penitus in mediā philosophiā;
    <cit>
      <quote lang="la">retrusa atque abdita,</quote>
      <bibl default="NO">
        <author>id.</author>
        ib. 1, 19, 87
      </bibl>
    </cit>
    al.: oppida,
    <hi rend="ital">remote</hi>
    , Cod. Th. 15, 1, 14. —
    <hi rend="ital">Comp</hi>
    . abditior,
    <bibl n="Perseus:abo:phi,2468,001:Conf. 5:5" default="NO">
      <author>Aug.</author>
      Conf. 5, 5
    </bibl>
    ;
    <bibl n="Perseus:abo:phi,2468,001:10:10" default="NO">10, 10</bibl>
    . —Sup. abditissimus, Aug. Enchir. c. 16. —
  </sense>
  <sense id="n52.5" n="II" level="1" opt="n">
    In the
    <hi rend="ital">neutr.</hi>
    :
    <orth extent="full" lang="la" opt="n">abdĭtum</orth>
    ,
    <itype opt="n">i</itype>
    ,
    <hi rend="ital">subst.</hi>
    :
    <cit>
      <quote lang="la">terrai abdita,</quote>
      <bibl n="Perseus:abo:phi,0550,001:6:809" default="NO" valid="yes">
        <author>Lucr.</author>
        6, 809
      </bibl>
    </cit>
    ; so,
    <cit>
      <quote lang="la">abdita rerum (=abditae res),</quote>
      <bibl default="NO">
        <author>Hor.</author>
        A.P. 49
      </bibl>
    </cit>
    :
    <cit>
      <quote lang="la">in abdito coire,</quote>
      <trans opt="n">
        <tr opt="n">in concealment, secretly</tr>
        ,
      </trans>
      <bibl n="Perseus:abo:phi,0978,001:8:13" default="NO" valid="yes">
        <author>Plin.</author>
        8, 5, 5, § 13
      </bibl>
    </cit>
    . —
    <hi rend="ital">Adv.</hi>
    :
    <orth extent="full" lang="la" opt="n">abdĭtē</orth>
    <hi rend="ital">secretly</hi>
    :
    <cit>
      <quote lang="la">latuisse,</quote>
      <bibl n="Perseus:abo:phi,0474,005:2:73:section=181" default="NO" valid="yes">
        <author>Cic.</author>
        Verr. 2, 2, 73, § 181
      </bibl>
    </cit>
    ; Ambros. Job et Dav. 1, 9, 29.
  </sense>
</entryFree>
        """
      val data = new LewisShortDictionaryData("abdo", tei)
      println(data.html)
    }
  }
}