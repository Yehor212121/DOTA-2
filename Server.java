import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/** Сервер «История Dota 2». Запуск: java Server [port] */
public class Server {
  static final String[][] PL = {
    {"Dendi","Завершил карьеру","Чемпион TI1. Король Pudge и всеобщий любимец: хук Dendi стал символом эпохи.","2007–2008|WolkeR Gaming;2008|Ks.int;2008–2009|DTS Gaming;2009|KS.Int;2009–2010|DTS Gaming;2010–2015|Natus Vincere;2015–н. в.|Natus Vincere;2019|Tigers (аренда);2019|The Pango (аренда);2019|The Pango;2020–2022|B8;2022–2025|B8"},
    {"Puppey","PARIVISION (тренер)","Чемпион TI1, капитан и стратег. Почти десять лет вёл Team Secret, а с 2026 года тренирует PARIVISION.","2007–2008|XsK;2008–2009|Ks.int;2009–2010|Blight.int;2010|Nirvana.int (дважды);2010–2011|NWO;2011|GosuGamers;2011–2014|Natus Vincere;2014–2026|Team Secret;2026–н. в.|PARIVISION (тренер)"},
    {"s4","Завершил карьеру","Лицо Rat Dota и чемпион TI3. Лидер, умевший читать игру на три хода вперёд.","2012|Team Empire;2012|The Tough Bananas;2012|Copenhagen Wolves (дважды);2012–2013|No Tidehunter;2013–2014|Alliance;2014–2015|Team Secret;2015–2016|Alliance;2016–2018|OG;2018–2019|Evil Geniuses;2019–2020|EG (неактивен);2020–2023|Alliance;2022|goonsquad;2022–2023|Alliance"},
    {"N0tail","Завершил карьеру","Дважды чемпион TI. Тот, кто сделал из андердогов легенду.","2012–2014|Fnatic;2014–2015|Team Secret;2015|Cloud9;2015|Monkey Business;2015–2021|OG;2019–2020|OG Seed (тренер);2021–2023|OG (неактивен);2025|OG (главный тренер)"},
    {"Miracle-","NGX (неактивен)","Мид-игрок, чемпион TI7. Агрессия и механика, ставшие эталоном.","2015|Balkan Bears;2015|Monkey Business;2015–2016|OG;2016–2019|Team Liquid;2019–2022|Nigma Galaxy;2022–2023|NGX (неактивен);2023–2025|Nigma Galaxy;2025–н. в.|NGX (неактивен)"},
    {"SumaiL","LGD Gaming","Чемпион TI5 в шестнадцать лет. Дерзкий вундеркинд.","2015–2016|Evil Geniuses;2016–2019|Evil Geniuses;2019|EG (неактивен);2019|Evil Geniuses;2019|Quincy Crew;2019–2020|EG (неактивен);2020|OG;2021|Liquid (замена);2021|OG;2021–2022|Team Secret;2022–2023|Nigma Galaxy;2023|NGX (неактивен);2023|Team Aster (аренда);2023–2026|Nigma Galaxy;2026|Nigma Galaxy;2026–н. в.|LGD Gaming"},
    {"Ana","Завершил карьеру","Керри OG: хладнокровие, вытащившее два Эгиса подряд.","2013|Mobility Gaming;2016|iG (запас);2016–2017|OG;2018|Team World;2018|Echo International;2018|OG;2018–2019|OG (неактивен);2019–2020|OG;2020–2021|OG (неактивен);2021|OG;2021–2022|Завершил карьеру;2022|T1"},
    {"Topson","OG","Мид OG на TI8 и TI9 — креатив и стабильность.","2017|SFTe-sports;2017|No Rats;2017–2018|5 Anchors;2018–2021|OG;2021–2023|OG (неактивен);2022|T1 (замена);2023–2024|Tundra Esports;2024–2026|Завершил карьеру;2026|LGD Gaming;2026–н. в.|OG"},
    {"Ceb","Завершил карьеру","Столп команды, поддерживавший её на пути к двум титулам.","2011–2012|Team Shakira;2012|Western Wolves;2012|Team Shakira;2012|Mortal Teamwork;2012–2013|DD.Dota;2013|Quantic Gaming;2013|DD.Dota;2013–2014|Sigma.int;2014|TF;2014–2015|Denial eSports;2015|Basically Unknown;2015|Team FIRE;2015|Summer's Rift;2015|Alliance;2015|Monkey Freedom;2015–2016|Kaipi;2016–2018|OG (тренер);2018–2020|OG;2020|OG (неактивен);2020–2021|OG;2021–2023|Завершил карьеру;2023–2025|OG;2025|OG"},
    {"Arteezy","Завершил карьеру","Чемпион TI5, король фарма и один из самых узнаваемых керри.","2013|Kaipi;2013|Take Five;2014|SadBoys;2014–2015|Evil Geniuses;2015|Team Secret;2015–2016|Evil Geniuses;2016|Team Secret;2016–2022|Evil Geniuses;2022–2024|Shopify Rebellion;2024–2025|SR (неактивен)"},
    {"KuroKy","NGX (тренер)","Капитан Team Liquid, чемпион TI7. Позже вёл Nigma Galaxy, а с 2024 года — тренер NGX.","2010|Online Kingdom (DotA);2011|GosuGamers.net;2012|uebelst-gamynG;2012|mouz (замена);2012|mousesports;2012|Virtus.pro;2012|The GD B-Team;2012|Team Zero;2012–2013|mousesports;2013–2014|Natus Vincere;2014–2015|Team Secret;2015|5Jungz;2015–2019|Team Liquid;2019–2024|Nigma Galaxy;2024–н. в.|NGX (тренер)"},
    {"Yatoro","Team Spirit","Керри Spirit и трёхкратный чемпион TI (2021, 2023, 2026) — первый игрок в истории с тремя Эгидами.","2020|Yellow Submarine;2020–2024|Team Spirit;2024–2025|TSpirit (неактивен);2025–н. в.|Team Spirit"},
    {"Collapse","Team Spirit (неактивен)","Оффлейнер и архитектор игры Spirit. Три Эгиды (2021, 2023, 2026) — первый трёхкратный чемпион TI.","2020|Cascade;2020|Yellow Submarine;2020–2026|Team Spirit;2026–н. в.|TSpirit (неактивен)"}
  };
  static final String[][] HR = {
    {"Invoker","Allstars","Сложнейший маг: десять заклинаний из трёх сфер."},
    {"Arc Warden","~2016","Двойник, переписавший понятие микроконтроля."},
    {"Underlord","~2016","Танк с властью над позицией и линиями."},
    {"Pangolier","~2018","Перекатывающийся дуэлянт — стиль и дерзость."},
    {"Void Spirit","~2020","Мобильный маг с четырьмя стихиями."},
    {"Primal Beast","~2021","Мощный инициатор с яростью и бросками."},
    {"Muerta","2023","Охотница с двумя формами: призрак и дробовик."},
    {"Ringmaster","22 августа 2024","Саппорт-шоумен: каждая драка превращается в цирковое представление."},
    {"Kez","7 ноября 2024","Ловкий воин-птица, переключающийся между двумя боевыми стилями: катана и сай."},
    {"Largo","15 декабря 2025","Лягушка-бард: ритм его песни диктует темп всей драки."}
  };
  static final String[][] PT = {
    {"6.78","Эпоха Reborn-предшественников: классический темп."},
    {"6.82","Баланс, который закалил TI4–TI5."},
    {"6.88","Огромное обновление героев и предметов."},
    {"7.00 «Новое путешествие»","Дерево талантов, аванпосты и новый ритм игры."},
    {"7.07","Перестройка предметов и героев после «Нового пути»."},
    {"7.2x","Нейтральные предметы и новые правила вмешательства."},
    {"7.33 «Новые рубежи»","Расширенная карта, новые объекты и ритм."}
  };
  static final String[][] MO = {
    {"TI1 · 2011 · Кёльн","Путь NaVi","Natus Vincere обыграли EHOME 3:1 и забрали миллион из призового фонда в 1,6 млн долларов. Состав: Dendi, Puppey, XBOCT, LighTofHeaven, Funn1k.","ti1.jpg"},
    {"TI2 · 2012 · Сиэтл","Invictus Gaming","Китайцы одолели Natus Vincere 3:1 и увезли первую Эгиду в Азию. Состав: Ferrari_430, YYF, Zhou, ChuaN, Faith.","ti2.jpg"},
    {"TI3 · 2013 · Сиэтл","Alliance","Победа над Natus Vincere 3:2 в финале, вошедшем в историю как триумф Rat Dota. Состав: s4, AdmiralBulldog, Loda, EGM, Akke.","ti3.jpg"},
    {"TI4 · 2014 · Сиэтл","Newbee","Победа над Vici Gaming 3:1 в финале двух китайских команд, при рекордном на тот момент призовом фонде около 10,9 млн долларов. Состав: Hao, Mu, xiao8, Banana, SanSheng.","ti4.jpg"},
    {"TI5 · 2015 · Сиэтл","Evil Geniuses","Победа над CDEC Gaming 3:1 и первая Эгида для американской команды. Состав: Arteezy, SumaiL, Universe, Aui_2000, Fear.","ti5.jpg"},
    {"TI6 · 2016 · Сиэтл","Wings Gaming","Победа над Digital Chaos 3:1 — китайская команда поднимает Эгиду при призовом фонде свыше 20 млн долларов. Состав: bLink, shadow, y`, iceiceice, Faith_bian.","ti6.jpg"},
    {"TI7 · 2017 · Сиэтл","Team Liquid","Чистая победа над Newbee 3:0. Первая Эгида Liquid. Состав: Miracle-, MinD_ContRoL, GH, KuroKy, MATUMBAMAN.","ti7.jpg"},
    {"TI8 · 2018 · Ванкувер","OG","Победа над PSG.LGD 3:2 — история андердогов, ставшая легендой. Состав: N0tail, Ana, Topson, Ceb, JerAx.","ti8.jpg"},
    {"TI9 · 2019 · Шанхай","OG","Победа над Team Liquid 3:1 и вторая Эгида подряд — первая такая серия в истории TI, при рекордном призовом фонде около 34,3 млн долларов. Состав тот же: N0tail, Ana, Topson, Ceb, JerAx.","ti9.jpg"},
    {"TI10 · 2021 · Бухарест","Team Spirit","Победа над PSG.LGD 3:2 и первая Эгида Spirit. Состав: Yatoro, TORONTOTOKYO, Collapse, Mira, Miposhka.","ti10.jpg"},
    {"TI11 · 2022 · Сингапур","Tundra Esports","Победа над Team Secret 3:2 в напряжённом финале. Состав: Skiter, Nine, 33, Saksa, Sneyking.","ti11.jpg"},
    {"TI12 · 2023 · Сиэтл","Team Spirit","Победа над Gaimin Gladiators 3:0 — вторая Эгида Spirit. Состав: Yatoro, Larl, Collapse, Mira, Miposhka.","ti12.jpg"},
    {"TI13 · 2024 · Копенгаген","Team Liquid","Победа над Gaimin Gladiators 3:0 и вторая Эгида Liquid спустя семь лет после первой. Состав: 33, Nisha, iNSaNiA, miCKe, Boxi.","ti13.jpg"},
    {"TI14 · 2025 · Гамбург","Team Falcons","Победа над Xtreme Gaming 3:2 в пятикарточной драме и первый титул клуба. Состав: Skiter, Malr1ne, ATF, Cr1t-, Sneyking.","ti14.jpg"},
    {"TI15 · 2026 · Шанхай","Team Spirit","Победа над TEAM VISION (PARIVISION) 3:2 — третья Эгида, первая в истории TI. Состав: Yatoro, Larl, Collapse, not me, rue (тренеры: Miposhka и MiLAN).","ti15.jpg"}
  };
  static final String[][] TL = {
    {"2011","TI1: NaVi, $1,6 млн"},
    {"2012","TI2: Invictus Gaming"},
    {"2013","Релиз Dota 2 и Rat Dota на TI3"},
    {"2014","TI4: Newbee, $10,9 млн"},
    {"2015","Reborn на Source 2; TI5: EG, $18,4 млн"},
    {"2016","TI6: Wings Gaming; патч 7.00"},
    {"2017","TI7: Team Liquid"},
    {"2018","TI8: OG, Ванкувер"},
    {"2019","TI9: OG, Шанхай, $34,3 млн"},
    {"2021","TI10: Team Spirit, Бухарест, $40 млн"},
    {"2022","TI11: Tundra Esports, Сингапур"},
    {"2023","TI12: Team Spirit; патч 7.33"},
    {"2024","TI13: Team Liquid, Копенгаген"},
    {"2025","TI14: Team Falcons"},
    {"2026","TI 2026: Team Spirit — третья Эгида, Шанхай"}
  };

  static String esc(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }

  static String arr(String[][] a) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < a.length; i++) {
      if (i > 0) sb.append(',');
      sb.append('[');
      for (int j = 0; j < a[i].length; j++) {
        if (j > 0) sb.append(',');
        sb.append('"').append(esc(a[i][j])).append('"');
      }
      sb.append(']');
    }
    return sb.append(']').toString();
  }

  static String json() {
    return "{\"pl\":" + arr(PL) + ",\"hr\":" + arr(HR) + ",\"pt\":" + arr(PT)
        + ",\"mo\":" + arr(MO) + ",\"tl\":" + arr(TL) + "}";
  }

  static void send(HttpExchange e, int code, String type, byte[] body) throws java.io.IOException {
    e.getResponseHeaders().set("Content-Type", type + "; charset=utf-8");
    e.sendResponseHeaders(code, body.length);
    try (java.io.OutputStream os = e.getResponseBody()) { os.write(body); }
  }

  public static void main(String[] args) throws Exception {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
    HttpServer s = null;
    for (int p = port; p < port + 20 && s == null; p++) {
      try { s = HttpServer.create(new InetSocketAddress(p), 0); port = p; } catch (java.net.BindException ex) { }
    }
    if (s == null) { System.out.println("No free port found"); return; }
    s.createContext("/api/data", e -> send(e, 200, "application/json", json().getBytes(StandardCharsets.UTF_8)));
    s.createContext("/", e -> {
      if (!e.getRequestURI().getPath().equals("/")) { send(e, 404, "text/plain", "404".getBytes()); return; }
      send(e, 200, "text/html", Files.readAllBytes(Paths.get("index.html")));
    });
    s.createContext("/img/", e -> {
      String n = e.getRequestURI().getPath().substring(5);
      Path f = Paths.get("img", n);
      if (n.contains("..") || n.contains("/") || !Files.isRegularFile(f)) { send(e, 404, "text/plain", "404".getBytes()); return; }
      send(e, 200, n.endsWith(".png") ? "image/png" : "image/jpeg", Files.readAllBytes(f));
    });
    s.start();
    System.out.println("Open http://localhost:" + port);
  }
}
