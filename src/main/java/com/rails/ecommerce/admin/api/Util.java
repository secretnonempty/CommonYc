package com.rails.ecommerce.admin.api;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.util.ParserException;

/*  22:    */ public class Util
/*  23:    */ {
/*  24: 54 */   private static Map<String, String> cityName2Code = new HashMap();
/*  25:    */   public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
/*  26:    */   public static final String DATE_PART_FORMAT = "yyyy-MM-dd";
/*  27:    */   public static final String TIME_PART_FORMAT = "HH:mm:ss.SSS";
/*  28:    */   
/*  29:    */   static
/*  30:    */   {
/*  31: 56 */     String[] city = Constants.CITYS.split("@");
/*  32: 57 */     for (String tmp : city) {
/*  33: 58 */       if (!tmp.isEmpty())
/*  34:    */       {
/*  35: 60 */         String[] temp = tmp.split("\\|");
/*  36: 61 */         cityName2Code.put(temp[1], temp[2]);
/*  37:    */       }
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41: 68 */   public static final DateFormat default_date_format = new SimpleDateFormat("yyyy-MM-dd");
/*  42:    */   
/*  43:    */   public static String getCurDateTime()
/*  44:    */   {
/*  45: 77 */     return getCurDateTime("yyyy-MM-dd HH:mm:ss");
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static String getCurDate()
/*  49:    */   {
/*  50: 86 */     return getCurDateTime("yyyy-MM-dd");
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static String getCurDateTime(String formatStr)
/*  54:    */   {
/*  55: 96 */     SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
/*  56: 97 */     String now = sdf.format(new Date());
/*  57: 98 */     return now;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static String getCityCode(String cityName)
/*  61:    */   {
/*  62:102 */     return (String)cityName2Code.get(cityName);
/*  63:    */   }
/*  64:    */   
///*  65:    */   public static List<TrainQueryInfo> parserQueryInfo(String response, String startDate)
///*  66:    */   {
///*  67:113 */     List<TrainQueryInfo> tqis = new ArrayList();
///*  68:114 */     response = response.replaceAll("&nbsp;", "");
///*  69:115 */     String[] st = response.split(",");
///*  70:117 */     for (int i = 0; i < st.length;)
///*  71:    */     {
///*  72:118 */       st[i] = st[i].trim();
///*  73:119 */       if (st[i].startsWith("<span"))
///*  74:    */       {
///*  75:120 */         TrainQueryInfo tqi = new TrainQueryInfo();
///*  76:121 */         tqi.setTrainDate(startDate);
///*  77:122 */         String info1 = st[(i++)];
///*  78:    */         try
///*  79:    */         {
///*  80:124 */           parserSpan(info1, tqi);
///*  81:125 */           String[] temp2 = st[(i++)].split("<br>");
///*  82:126 */           if (temp2[0].startsWith("<img")) {
///*  83:127 */             tqi.setFromStation(temp2[0].split(">")[1]);
///*  84:    */           } else {
///*  85:129 */             tqi.setFromStation(temp2[0]);
///*  86:    */           }
///*  87:131 */           tqi.setStartTime(temp2[1]);
///*  88:132 */           String[] temp3 = st[(i++)].split("<br>");
///*  89:133 */           if (temp3[0].startsWith("<img")) {
///*  90:134 */             tqi.setToStation(temp3[0].split(">")[1]);
///*  91:    */           } else {
///*  92:136 */             tqi.setToStation(temp3[0]);
///*  93:    */           }
///*  94:138 */           tqi.setEndTime(temp3[1]);
///*  95:139 */           tqi.setTakeTime(st[(i++)]);
///*  96:140 */           tqi.setBuss_seat(parserFont(st[(i++)]));
///*  97:141 */           tqi.setBest_seat(parserFont(st[(i++)]));
///*  98:142 */           tqi.setOne_seat(parserFont(st[(i++)]));
///*  99:143 */           tqi.setTwo_seat(parserFont(st[(i++)]));
///* 100:144 */           tqi.setVag_sleeper(parserFont(st[(i++)]));
///* 101:145 */           tqi.setSoft_sleeper(parserFont(st[(i++)]));
///* 102:146 */           tqi.setHard_sleeper(parserFont(st[(i++)]));
///* 103:147 */           tqi.setSoft_seat(parserFont(st[(i++)]));
///* 104:148 */           tqi.setHard_seat(parserFont(st[(i++)]));
///* 105:149 */           tqi.setNone_seat(parserFont(st[(i++)]));
///* 106:150 */           tqi.setOther_seat(parserFont(st[(i++)]));
///* 107:151 */           String info2 = st[(i++)];
///* 108:152 */           int idx = info2.indexOf("getSelected('");
///* 109:153 */           if (idx > 0)
///* 110:    */           {
///* 111:155 */             info2 = info2.substring(idx + 13);
///* 112:    */             
///* 113:157 */             String[] pcs = info2.substring(0, info2.indexOf('\'')).split("#");
///* 114:158 */             if (pcs.length >= 13)
///* 115:    */             {
///* 116:159 */               int j = 0;
///* 117:160 */               tqi.setTrainNo(pcs[(j++)]);
///* 118:161 */               tqi.setTakeTime(pcs[(j++)]);
///* 119:162 */               tqi.setStartTime(pcs[(j++)]);
///* 120:163 */               tqi.setTrainCode(pcs[(j++)]);
///* 121:164 */               tqi.setFromStationCode(pcs[(j++)]);
///* 122:165 */               tqi.setToStationCode(pcs[(j++)]);
///* 123:166 */               tqi.setEndTime(pcs[(j++)]);
///* 124:167 */               tqi.setFromStation(pcs[(j++)]);
///* 125:168 */               tqi.setToStation(pcs[(j++)]);
///* 126:169 */               tqi.setFromStationNo(pcs[(j++)]);
///* 127:170 */               tqi.setToStationNo(pcs[(j++)]);
///* 128:171 */               tqi.setYpInfo(pcs[(j++)]);
///* 129:172 */               tqi.setMmStr(pcs[(j++)]);
///* 130:173 */               tqi.setLocationCode(pcs[(j++)]);
///* 131:    */             }
///* 132:    */           }
///* 133:    */         }
///* 134:    */         catch (ParserException e)
///* 135:    */         {
///* 136:177 */           e.printStackTrace();
///* 137:    */         }
///* 138:179 */         tqis.add(tqi);
///* 139:    */       }
///* 140:    */       else
///* 141:    */       {
///* 142:181 */         i++;
///* 143:    */       }
///* 144:    */     }
///* 145:184 */     return tqis;
///* 146:    */   }
/* 147:    */   
/* 148:    */   private static String parserFont(String font)
/* 149:    */     throws ParserException
/* 150:    */   {
/* 151:195 */     String ans = font;
/* 152:196 */     if (font.startsWith("<font"))
/* 153:    */     {
/* 154:197 */       int beginIndex = font.indexOf("'>");
/* 155:198 */       int endIndex = font.indexOf("</");
/* 156:199 */       ans = font.substring(beginIndex + 2, endIndex);
/* 157:    */     }
/* 158:202 */     return ans;
/* 159:    */   }
/* 160:    */   
///* 161:    */   private static void parserSpan(String responseBody, TrainQueryInfo tqi)
///* 162:    */     throws ParserException
///* 163:    */   {
///* 164:214 */     Parser parser = new Parser();
///* 165:215 */     parser.setInputHTML(responseBody);
///* 166:216 */     NodeVisitor visitor = new NodeVisitor()
///* 167:    */     {
///* 168:    */       public void visitTag(Tag tag)
///* 169:    */       {
///* 170:218 */         this.val$tqi.setTrainNo(tag.getChildren().toHtml());
///* 171:219 */         Vector<?> atts = tag.getAttributesEx();
///* 172:220 */         for (int i = 0; i < atts.size(); i++)
///* 173:    */         {
///* 174:221 */           Attribute att = (Attribute)atts.get(i);
///* 175:222 */           String name = att.getName();
///* 176:223 */           if ("onmouseover".equals(name))
///* 177:    */           {
///* 178:224 */             String[] temp = att.getValue().split("'");
///* 179:225 */             String[] subTemp = temp[1].split("#");
///* 180:226 */             this.val$tqi.setTrainCode(subTemp[0]);
///* 181:227 */             this.val$tqi.setFromStationCode(subTemp[1]);
///* 182:228 */             this.val$tqi.setToStationCode(subTemp[2]);
///* 183:    */           }
///* 184:    */         }
///* 185:    */       }
///* 186:232 */     };
///* 187:233 */     parser.visitAllNodesWith(visitor);
///* 188:    */   }
/* 189:    */   
/* 190:    */   public static String removeTagFromHtml(String content)
/* 191:    */   {
/* 192:244 */     String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
/* 193:    */     
/* 194:246 */     String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
/* 195:    */     
/* 196:248 */     String regEx_html = "<[^>]+>";
/* 197:    */     
/* 198:250 */     String temp = content;
/* 199:251 */     if ((content == null) || (content.isEmpty())) {
/* 200:252 */       return "ERROR";
/* 201:    */     }
/* 202:255 */     temp = temp.replaceAll(regEx_script, "");
/* 203:    */     
/* 204:257 */     temp = temp.replaceAll(regEx_style, "");
/* 205:    */     
/* 206:259 */     temp = temp.replaceAll(regEx_html, "");
/* 207:    */     
/* 208:261 */     temp = temp.replaceAll("\\s+", " ");
/* 209:    */     
/* 210:263 */     return temp.trim();
/* 211:    */   }
/* 212:    */   
/* 213:    */   public static String getMessageFromHtml(String content)
/* 214:    */   {
/* 215:268 */     String regEx_msg = "var\\s+message\\s+=\\s+\"([\\S|\\s]*?)\"";
/* 216:269 */     if (content == null) {
/* 217:270 */       return "ERROR";
/* 218:    */     }
/* 219:272 */     String temp = content.trim();
/* 220:273 */     Pattern p = null;
/* 221:274 */     Matcher m = null;
/* 222:275 */     p = Pattern.compile(regEx_msg);
/* 223:276 */     m = p.matcher(temp);
/* 224:    */     try
/* 225:    */     {
/* 226:278 */       if (m.find()) {
/* 227:279 */         temp = m.group(1);
/* 228:    */       }
/* 229:    */     }
/* 230:    */     catch (Exception e)
/* 231:    */     {
/* 232:283 */       e.printStackTrace();
/* 233:    */     }
/* 234:285 */     return temp;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public static String getLoginErrorMessage(String content)
/* 238:    */   {
/* 239:289 */     int beginIndex = content.indexOf("换一张 &nbsp; ");
/* 240:290 */     int endIndex = content.indexOf(" &nbsp; &nbsp; 登录");
/* 241:291 */     String subStr = "ERROR";
/* 242:292 */     if (beginIndex + 11 < endIndex) {
/* 243:293 */       subStr = content.substring(beginIndex + 11, endIndex);
/* 244:    */     }
/* 245:295 */     return subStr;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public static int getHour2Min(String hour)
/* 249:    */   {
/* 250:299 */     int min = 0;
/* 251:300 */     String[] hm = hour.split(":");
/* 252:301 */     if (hm.length < 2)
/* 253:    */     {
/* 254:302 */       min = Integer.parseInt(hour);
/* 255:    */     }
/* 256:    */     else
/* 257:    */     {
/* 258:304 */       int h = Integer.parseInt(hm[0]) * 60;
/* 259:305 */       int m = Integer.parseInt(hm[1]);
/* 260:306 */       min = h + m;
/* 261:    */     }
/* 262:308 */     return min;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static String formatInfo(String info)
/* 266:    */   {
/* 267:312 */     return getCurDateTime() + " : " + info + "\n";
/* 268:    */   }
/* 269:    */   
/* 270:    */   public static String StrFormat(String pattern, Object... arguments)
/* 271:    */   {
/* 272:316 */     Object[] argumentStr = new String[arguments.length];
/* 273:317 */     for (int i = 0; i < argumentStr.length; i++) {
/* 274:318 */       argumentStr[i] = arguments[i].toString();
/* 275:    */     }
/* 276:320 */     return MessageFormat.format(pattern, argumentStr);
/* 277:    */   }






/* 278:    */ }


/* Location:           C:\Users\Administrator\Desktop\GoHome_2013版\original-ticketRobot-0.0.1-SNAPSHOT.jar
 * Qualified Name:     com.ywh.train.Util
 * JD-Core Version:    0.7.0.1
 */