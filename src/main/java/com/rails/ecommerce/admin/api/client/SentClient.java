package com.rails.ecommerce.admin.api.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.rails.ecommerce.admin.api.Constants;
import com.rails.ecommerce.admin.api.Util;

/*  40:    */ public class SentClient
/*  41:    */ {
/*  42: 67 */   public static String JSESSIONID = null;
/*  43: 68 */   public static String BIGipServerotsweb = null;
/*  44: 69 */   Logger log = Logger.getLogger(getClass());
/*  45: 70 */   private DefaultHttpClient httpclient = null;
/*  46:    */   
/*  47:    */   public SentClient(DefaultHttpClient client)
/*  48:    */   {
/*  49: 76 */     this.httpclient = client;
/*  50: 77 */     client.getParams().setParameter("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; TEN)");
/*  51:    */   }
/*  52:    */   
				
				public String getUrl(String urlName, String jxFlag) {
					String request_url = null;
					if("1".equals(jxFlag)) {
						  request_url = Constants.LQ_URL + urlName;
					  } else if ("2".equals(jxFlag)) {
						  request_url = Constants.XY_URL + urlName;
					  } else if ("3".equals(jxFlag)) {
						  request_url = Constants.JDF_URL + urlName;
					  } else if ("4".equals(jxFlag)) {
						  request_url = Constants.HJ_URL + urlName;
					  } else if ("5".equals(jxFlag)) {
						  request_url = Constants.XF_URL + urlName;
					  } else {
						  request_url = Constants.LQ_URL + urlName;
					  }
					return request_url;
				}

				public String getStudentInfo(String xxzh, String jxFlag)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------book start-------------------");
///* 117:136 */     Result rs = new Result();
				  String request_url = getUrl(Constants.STUINFO_URL, jxFlag);
/* 118:137 */     HttpPost post = new HttpPost(request_url);
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("xxzh", xxzh));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", request_url);
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------book end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }

				/**
				 * 获得预约的车次记录
				 * 
				 * @author gxl DateTime 2015-1-12 下午2:06:04
				 * @return
				 * @throws Exception
				 */
				public String getOrderRecord(String xxzh, String jxFlag)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------getOrderRecord start-------------------");
///* 117:136 */     Result rs = new Result();
				  String request_url = getUrl(Constants.ORDER_RECORD_URL, jxFlag);
/* 118:137 */     HttpPost post = new HttpPost(request_url);
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("xxzh", xxzh));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", request_url);
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------getOrderRecord end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
				/**
				 * 获得预约的车次记录
				 * 
				 * @author gxl DateTime 2015-1-12 下午2:06:04
				 * @return
				 * @throws Exception
				 */
				public String cancelOrder(String xxzh, String jlcbh, String yyrq, String xnsd, String jxFlag)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------cancelOrder start-------------------");
///* 117:136 */     Result rs = new Result();
				  String request_url = getUrl(Constants.CANCEL_ORDER_URL, jxFlag);
/* 118:137 */     HttpPost post = new HttpPost(request_url);
				  StringBuffer sb = new StringBuffer();
				  String params = sb.append(xxzh).append(".").append(jlcbh).append(".").append(yyrq).append(".").append(xnsd).append(".").toString();
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("params", params));
				  formparams.add(new BasicNameValuePair("xxzh", xxzh));
				  formparams.add(new BasicNameValuePair("xxzh", xxzh));
				  formparams.add(new BasicNameValuePair("jlcbh", jlcbh));
				  formparams.add(new BasicNameValuePair("yyrq", yyrq));
				  formparams.add(new BasicNameValuePair("xnsd", xnsd));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", request_url);
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------cancelOrder end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
				/**
				 * 获得验证码
				 * 
				 * @author gxl DateTime 2015-1-12 下午2:06:04
				 * @return
				 * @throws Exception
				 */
				public String getRandomCode(String xxzh, String jxFlag)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------getRandomCode start-------------------");
///* 117:136 */     Result rs = new Result();
				  String request_url = getUrl(Constants.CREATE_RANDOM_PRE_URL, jxFlag);
				  request_url = request_url + xxzh + Constants.CREATE_RANDOM_END_URL;
				  System.out.println(request_url);
/* 118:137 */     HttpPost post = new HttpPost(request_url);
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("xxzh", xxzh));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", request_url);
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------getRandomCode end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
				/**
				 * 获得预约的车次记录
				 * 
				 * @author gxl DateTime 2015-1-12 下午2:06:04
				 * @return
				 * @throws Exception
				 */
				public String checkCode(String xxzh, String code, String jxFlag)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------checkCode start-------------------");
///* 117:136 */     Result rs = new Result();
				  String request_url = getUrl(Constants.CHECK_RANDOM_URL, jxFlag);
/* 118:137 */     HttpPost post = new HttpPost(request_url);
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("code", code));
				  formparams.add(new BasicNameValuePair("xxzh", xxzh));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", request_url);
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------checkCode end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
				public String getUserInfo(String jcy_count)
/* 115:    */   {
/* 116:135 */     this.log.debug("-------------------getUserInfo start-------------------");
///* 117:136 */     Result rs = new Result();
/* 118:137 */     HttpPost post = new HttpPost(Constants.USERINFO_URL);
/* 119:138 */     List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 120:139 */     formparams.add(new BasicNameValuePair("username", jcy_count));
///* 121:140 */     formparams.add(new BasicNameValuePair("station_train_code", train.getTrainNo()));
///* 122:141 */     formparams.add(new BasicNameValuePair("train_date", startDate));
///* 123:142 */     formparams.add(new BasicNameValuePair("seattype_num", ""));
///* 124:143 */     formparams.add(new BasicNameValuePair("from_station_telecode", train.getFromStationCode()));
///* 125:144 */     formparams.add(new BasicNameValuePair("to_station_telecode", train.getToStationCode()));
///* 126:145 */     formparams.add(new BasicNameValuePair("include_student", "00"));
///* 127:146 */     formparams.add(new BasicNameValuePair("from_station_telecode_name", train.getFromStation()));
///* 128:147 */     formparams.add(new BasicNameValuePair("to_station_telecode_name", train.getToStation()));
///* 129:148 */     formparams.add(new BasicNameValuePair("round_train_date", startDate));
///* 130:149 */     formparams.add(new BasicNameValuePair("round_start_time_str", rangDate));
///* 131:150 */     formparams.add(new BasicNameValuePair("single_round_type", "1"));
///* 132:151 */     formparams.add(new BasicNameValuePair("train_pass_type", "QB"));
///* 133:152 */     formparams.add(new BasicNameValuePair("train_class_arr", "QB#D#Z#T#K#QT#"));
///* 134:153 */     formparams.add(new BasicNameValuePair("start_time_str", rangDate));
///* 135:154 */     formparams.add(new BasicNameValuePair("lishi", train.getTakeTime()));
///* 136:155 */     formparams.add(new BasicNameValuePair("train_start_time", train.getStartTime()));
///* 137:156 */     formparams.add(new BasicNameValuePair("trainno4", train.getTrainCode()));
///* 138:157 */     formparams.add(new BasicNameValuePair("arrive_time", train.getEndTime()));
///* 139:158 */     formparams.add(new BasicNameValuePair("from_station_name", train.getFromStation()));
///* 140:159 */     formparams.add(new BasicNameValuePair("to_station_name", train.getToStation()));
///* 141:160 */     formparams.add(new BasicNameValuePair("from_station_no", train.getFromStationNo()));
///* 142:161 */     formparams.add(new BasicNameValuePair("to_station_no", train.getToStationNo()));
///* 143:162 */     formparams.add(new BasicNameValuePair("ypInfoDetail", train.getYpInfo()));
///* 144:163 */     formparams.add(new BasicNameValuePair("mmStr", train.getMmStr()));
///* 145:164 */     formparams.add(new BasicNameValuePair("locationCode", train.getLocationCode()));
/* 147:    */     String ans = null;  
				  try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
					
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", "http://115.28.226.254:8008/User/GetUserInfoByUserId");
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setCode("100");
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setCode("12");
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.debug("-------------------getUserInfo end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
				public String getSysTime()
/* 115:    */   {
/* 116:135 */     this.log.info("-------------------getSysTime start-------------------");
/* 117:136 */     String ans = null;
/* 118:137 */     HttpPost post = new HttpPost(Constants.TIME_URL);
				  List<NameValuePair> formparams = new ArrayList<NameValuePair>();
/* 147:    */     try
/* 148:    */     {
/* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
/* 150:168 */       post.setEntity(uef);
/* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
/* 152:170 */       String responseBody = null;
/* 153:    */       try
/* 154:    */       {
/* 155:172 */         post.setHeader("Referer", "http://106.37.230.254:8008/api/GetServiceDate");
/* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
/* 157:    */         {
/* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
/* 159:    */           {
/* 160:175 */             boolean isRedirect = false;
/* 161:    */             try
/* 162:    */             {
/* 163:177 */               isRedirect = super.isRedirected(request, response, context);
/* 164:    */             }
/* 165:    */             catch (ProtocolException e)
/* 166:    */             {
/* 167:179 */               SentClient.this.log.error(e, e);
/* 168:    */             }
/* 169:181 */             if (!isRedirect)
/* 170:    */             {
/* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
/* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
/* 173:184 */                 return true;
/* 174:    */               }
/* 175:    */             }
/* 176:187 */             return isRedirect;
/* 177:    */           }
/* 178:189 */         });
/* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
/* 180:191 */         ans = Util.removeTagFromHtml(responseBody);
/* 195:    */       }
/* 196:    */       catch (Exception e)
/* 197:    */       {
/* 198:203 */         this.log.error(e, e);
/* 199:    */       }
/* 210:216 */       this.log.info("-------------------getSysTime end-------------------");
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:206 */       this.log.error(e, e);
/* 215:    */     }
/* 229:217 */     return ans;
/* 230:    */   }
				
///*  53:    */   public String getToken()
///*  54:    */   {
///*  55: 88 */     this.log.debug("-------------------get token start-------------------");
///*  56: 89 */     HttpGet get = new HttpGet(Constants.GET_TOKEN_URL);
///*  57: 90 */     String token = null;
///*  58: 91 */     BufferedReader br = null;
///*  59:    */     try
///*  60:    */     {
///*  61: 93 */       HttpResponse response = this.httpclient.execute(get);
///*  62: 94 */       HttpEntity entity = response.getEntity();
///*  63: 95 */       br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
///*  64:    */       
///*  65: 97 */       String line = null;
///*  66: 98 */       while ((line = br.readLine()) != null) {
///*  67: 99 */         if (line.indexOf("org.apache.struts.taglib.html.TOKEN") > -1) {
///*  68:100 */           token = line;
///*  69:    */         }
///*  70:    */       }
///*  71:103 */       if (token != null)
///*  72:    */       {
///*  73:104 */         int start = token.indexOf("value=\"");
///*  74:105 */         int end = token.indexOf("\"></div>");
///*  75:106 */         token = token.substring(start + 7, end);
///*  76:    */       }
///*  77:    */       else
///*  78:    */       {
///*  79:108 */         this.log.warn("book tikte error, can't get token!");
///*  80:    */       }
///*  81:    */       try
///*  82:    */       {
///*  83:114 */         if (br != null) {
///*  84:115 */           br.close();
///*  85:    */         }
///*  86:    */       }
///*  87:    */       catch (IOException e)
///*  88:    */       {
///*  89:118 */         e.printStackTrace();
///*  90:    */       }
///*  91:121 */       this.log.debug("TOKEN = " + token);
///*  92:    */     }
///*  93:    */     catch (Exception e)
///*  94:    */     {
///*  95:111 */       this.log.error(e);
///*  96:    */     }
///*  97:    */     finally
///*  98:    */     {
///*  99:    */       try
///* 100:    */       {
///* 101:114 */         if (br != null) {
///* 102:115 */           br.close();
///* 103:    */         }
///* 104:    */       }
///* 105:    */       catch (IOException e)
///* 106:    */       {
///* 107:118 */         e.printStackTrace();
///* 108:    */       }
///* 109:    */     }
///* 110:122 */     this.log.debug("-------------------get token end-------------------");
///* 111:123 */     return token;
///* 112:    */   }
/* 113:    */   
///* 114:    */   public Result book(String rangDate, String startDate, TrainQueryInfo train)
///* 115:    */   {
///* 116:135 */     this.log.debug("-------------------book start-------------------");
///* 117:136 */     Result rs = new Result();
///* 118:137 */     HttpPost post = new HttpPost(Constants.BOOK_URL);
///* 119:138 */     List<NameValuePair> formparams = new ArrayList();
///* 120:139 */     formparams.add(new BasicNameValuePair("method", "submutOrderRequest"));
///* 121:140 */     formparams.add(new BasicNameValuePair("station_train_code", train.getTrainNo()));
///* 122:141 */     formparams.add(new BasicNameValuePair("train_date", startDate));
///* 123:142 */     formparams.add(new BasicNameValuePair("seattype_num", ""));
///* 124:143 */     formparams.add(new BasicNameValuePair("from_station_telecode", train.getFromStationCode()));
///* 125:144 */     formparams.add(new BasicNameValuePair("to_station_telecode", train.getToStationCode()));
///* 126:145 */     formparams.add(new BasicNameValuePair("include_student", "00"));
///* 127:146 */     formparams.add(new BasicNameValuePair("from_station_telecode_name", train.getFromStation()));
///* 128:147 */     formparams.add(new BasicNameValuePair("to_station_telecode_name", train.getToStation()));
///* 129:148 */     formparams.add(new BasicNameValuePair("round_train_date", startDate));
///* 130:149 */     formparams.add(new BasicNameValuePair("round_start_time_str", rangDate));
///* 131:150 */     formparams.add(new BasicNameValuePair("single_round_type", "1"));
///* 132:151 */     formparams.add(new BasicNameValuePair("train_pass_type", "QB"));
///* 133:152 */     formparams.add(new BasicNameValuePair("train_class_arr", "QB#D#Z#T#K#QT#"));
///* 134:153 */     formparams.add(new BasicNameValuePair("start_time_str", rangDate));
///* 135:154 */     formparams.add(new BasicNameValuePair("lishi", train.getTakeTime()));
///* 136:155 */     formparams.add(new BasicNameValuePair("train_start_time", train.getStartTime()));
///* 137:156 */     formparams.add(new BasicNameValuePair("trainno4", train.getTrainCode()));
///* 138:157 */     formparams.add(new BasicNameValuePair("arrive_time", train.getEndTime()));
///* 139:158 */     formparams.add(new BasicNameValuePair("from_station_name", train.getFromStation()));
///* 140:159 */     formparams.add(new BasicNameValuePair("to_station_name", train.getToStation()));
///* 141:160 */     formparams.add(new BasicNameValuePair("from_station_no", train.getFromStationNo()));
///* 142:161 */     formparams.add(new BasicNameValuePair("to_station_no", train.getToStationNo()));
///* 143:162 */     formparams.add(new BasicNameValuePair("ypInfoDetail", train.getYpInfo()));
///* 144:163 */     formparams.add(new BasicNameValuePair("mmStr", train.getMmStr()));
///* 145:164 */     formparams.add(new BasicNameValuePair("locationCode", train.getLocationCode()));
///* 146:165 */     BufferedReader br = null;
///* 147:    */     try
///* 148:    */     {
///* 149:167 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
///* 150:168 */       post.setEntity(uef);
///* 151:169 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
///* 152:170 */       String responseBody = null;
///* 153:    */       try
///* 154:    */       {
///* 155:172 */         post.setHeader("Referer", "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=init");
///* 156:173 */         this.httpclient.setRedirectStrategy(new DefaultRedirectStrategy()
///* 157:    */         {
///* 158:    */           public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
///* 159:    */           {
///* 160:175 */             boolean isRedirect = false;
///* 161:    */             try
///* 162:    */             {
///* 163:177 */               isRedirect = super.isRedirected(request, response, context);
///* 164:    */             }
///* 165:    */             catch (ProtocolException e)
///* 166:    */             {
///* 167:179 */               SentClient.this.log.error(e, e);
///* 168:    */             }
///* 169:181 */             if (!isRedirect)
///* 170:    */             {
///* 171:182 */               int responseCode = response.getStatusLine().getStatusCode();
///* 172:183 */               if ((responseCode == 301) || (responseCode == 302)) {
///* 173:184 */                 return true;
///* 174:    */               }
///* 175:    */             }
///* 176:187 */             return isRedirect;
///* 177:    */           }
///* 178:189 */         });
///* 179:190 */         responseBody = (String)this.httpclient.execute(post, responseHandler);
///* 180:191 */         String ans = Util.removeTagFromHtml(responseBody);
///* 181:193 */         if ((ans != null) && (ans.indexOf("提交订单") > 0))
///* 182:    */         {
///* 183:194 */           rs.setState((byte)100);
///* 184:195 */           rs.setMsg("");
///* 185:    */         }
///* 186:196 */         else if (responseBody.indexOf("目前您还有未处理的订单") > 0)
///* 187:    */         {
///* 188:197 */           rs.setState((byte)12);
///* 189:198 */           rs.setMsg("目前您还有未处理的订单，还不快去支付!");
///* 190:    */         }
///* 191:    */         else
///* 192:    */         {
///* 193:200 */           rs.setMsg(ans);
///* 194:    */         }
///* 195:    */       }
///* 196:    */       catch (Exception e)
///* 197:    */       {
///* 198:203 */         this.log.error(e, e);
///* 199:    */       }
///* 200:    */       try
///* 201:    */       {
///* 202:209 */         if (br != null) {
///* 203:210 */           br.close();
///* 204:    */         }
///* 205:    */       }
///* 206:    */       catch (IOException e)
///* 207:    */       {
///* 208:213 */         this.log.error(e, e);
///* 209:    */       }
///* 210:216 */       this.log.debug("-------------------book end-------------------");
///* 211:    */     }
///* 212:    */     catch (Exception e)
///* 213:    */     {
///* 214:206 */       this.log.error(e, e);
///* 215:    */     }
///* 216:    */     finally
///* 217:    */     {
///* 218:    */       try
///* 219:    */       {
///* 220:209 */         if (br != null) {
///* 221:210 */           br.close();
///* 222:    */         }
///* 223:    */       }
///* 224:    */       catch (IOException e)
///* 225:    */       {
///* 226:213 */         this.log.error(e, e);
///* 227:    */       }
///* 228:    */     }
///* 229:217 */     return rs;
///* 230:    */   }
/* 231:    */   
///* 232:    */   public Result submiOrderCheck(String randCode, List<UserInfo> users, TrainQueryInfo train)
///* 233:    */   {
///* 234:230 */     return submitOrder(randCode, users, train, true);
///* 235:    */   }
///* 236:    */   
///* 237:    */   public Result submiOrderConfirm(String randCode, List<UserInfo> users, TrainQueryInfo train)
///* 238:    */   {
///* 239:242 */     return submitOrder(randCode, users, train, false);
///* 240:    */   }
///* 241:    */   
///* 242:    */   private Result submitOrder(String randCode, List<UserInfo> users, TrainQueryInfo train, boolean isCheck)
///* 243:    */   {
///* 244:247 */     this.log.debug("-------------------submit order start-------------------");
///* 245:248 */     Result rs = new Result();
///* 246:249 */     HttpPost post = new HttpPost((isCheck ? Constants.SUBMIT_URL_CHECK : Constants.SUBMIT_URL_CONFIRM) + "&rand=" + randCode);
///* 247:    */     
///* 248:251 */     List<NameValuePair> formparams = new ArrayList();
///* 249:252 */     formparams.add(new BasicNameValuePair("method", isCheck ? "checkOrderInfo" : "confirmSingleForQueueOrder"));
///* 250:253 */     for (int i = 0; i < users.size(); i++) {
///* 251:254 */       formparams.add(new BasicNameValuePair("checkbox" + i, "" + i));
///* 252:    */     }
///* 253:256 */     formparams.add(new BasicNameValuePair("checkbox9", "Y"));
///* 254:257 */     formparams.add(new BasicNameValuePair("checkbox9", "Y"));
///* 255:258 */     formparams.add(new BasicNameValuePair("checkbox9", "Y"));
///* 256:259 */     formparams.add(new BasicNameValuePair("checkbox9", "Y"));
///* 257:260 */     formparams.add(new BasicNameValuePair("checkbox9", "Y"));
///* 258:261 */     for (UserInfo user : users) {
///* 259:262 */       formparams.add(new BasicNameValuePair("oldPassengers", user.getSimpleText()));
///* 260:    */     }
///* 261:264 */     for (int i = users.size(); i < 5; i++) {
///* 262:265 */       formparams.add(new BasicNameValuePair("oldPassengers", ""));
///* 263:    */     }
///* 264:267 */     formparams.add(new BasicNameValuePair("orderRequest.bed_level_order_num", "000000000000000000000000000000"));
///* 265:268 */     formparams.add(new BasicNameValuePair("orderRequest.cancel_flag", "1"));
///* 266:269 */     formparams.add(new BasicNameValuePair("orderRequest.end_time", train.getEndTime()));
///* 267:270 */     formparams.add(new BasicNameValuePair("orderRequest.from_station_name", train.getFromStation()));
///* 268:271 */     formparams.add(new BasicNameValuePair("orderRequest.from_station_telecode", train.getFromStationCode()));
///* 269:272 */     formparams.add(new BasicNameValuePair("orderRequest.id_mode", "Y"));
///* 270:273 */     formparams.add(new BasicNameValuePair("orderRequest.reserve_flag", "A"));
///* 271:274 */     formparams.add(new BasicNameValuePair("orderRequest.seat_type_code", ""));
///* 272:275 */     formparams.add(new BasicNameValuePair("orderRequest.seat_detail_type_code", ""));
///* 273:276 */     formparams.add(new BasicNameValuePair("orderRequest.start_time", train.getStartTime()));
///* 274:277 */     formparams.add(new BasicNameValuePair("orderRequest.station_train_code", train.getTrainNo()));
///* 275:278 */     formparams.add(new BasicNameValuePair("orderRequest.ticket_type_order_num", ""));
///* 276:279 */     formparams.add(new BasicNameValuePair("orderRequest.to_station_name", train.getToStation()));
///* 277:280 */     formparams.add(new BasicNameValuePair("orderRequest.to_station_telecode", train.getToStationCode()));
///* 278:281 */     formparams.add(new BasicNameValuePair("orderRequest.train_date", train.getTrainDate()));
///* 279:282 */     formparams.add(new BasicNameValuePair("orderRequest.train_no", train.getTrainCode()));
///* 280:283 */     String token = getToken();
///* 281:284 */     formparams.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
///* 282:285 */     for (UserInfo user : users) {
///* 283:286 */       formparams.add(new BasicNameValuePair("passengerTickets", user.getText()));
///* 284:    */     }
///* 285:288 */     for (int i = 1; i <= users.size(); i++)
///* 286:    */     {
///* 287:289 */       UserInfo user = (UserInfo)users.get(i - 1);
///* 288:290 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_cardno", user.getID()));
///* 289:291 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_cardtype", user.getCardType()));
///* 290:292 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_mobileno", user.getPhone()));
///* 291:293 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_name", user.getName()));
///* 292:294 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_seat", user.getSeatType()));
///* 293:295 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_seat_detail", user.getSeatPos()));
///* 294:296 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_seat_detail_select", user.getSeatPos()));
///* 295:297 */       formparams.add(new BasicNameValuePair("passenger_" + i + "_ticket", user.getTicketType()));
///* 296:    */     }
///* 297:299 */     formparams.add(new BasicNameValuePair("randCode", randCode));
///* 298:300 */     formparams.add(new BasicNameValuePair("rand", randCode));
///* 299:301 */     formparams.add(new BasicNameValuePair("textfield", "中文或拼音首字母"));
///* 300:302 */     formparams.add(new BasicNameValuePair("leftTicketStr", "1022903219406400001410229000003040300000"));
///* 301:303 */     if (isCheck) {
///* 302:304 */       formparams.add(new BasicNameValuePair("tFlag", "dc"));
///* 303:    */     }
///* 304:306 */     String responseBody = null;
///* 305:    */     try
///* 306:    */     {
///* 307:308 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
///* 308:309 */       post.setEntity(uef);
///* 309:310 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
///* 310:311 */       post.addHeader("Referer", "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
///* 311:312 */       post.addHeader("x-requested-with", "XMLHttpRequest");
///* 312:313 */       post.addHeader("Accept", "application/json, text/javascript, */*");
///* 313:314 */       responseBody = (String)this.httpclient.execute(post, responseHandler);
///* 314:315 */       String ans = Util.getMessageFromHtml(responseBody);
///* 315:318 */       if (((isCheck) && ("{\"checkHuimd\":\"Y\",\"check608\":\"Y\",\"msg\":\"\",\"errMsg\":\"Y\"}".equals(ans))) || ((!isCheck) && ("{\"errMsg\":\"Y\"}".equals(ans))))
///* 316:    */       {
///* 317:320 */         rs.setState((byte)100);
///* 318:321 */         rs.setMsg(isCheck ? "提交订票成功" : "确认订票成功");
///* 319:    */       }
///* 320:323 */       else if (ans.contains("由于您取消次数过多"))
///* 321:    */       {
///* 322:324 */         rs.setState((byte)13);
///* 323:325 */         rs.setMsg(ans);
///* 324:    */       }
///* 325:326 */       else if (ans.contains("验证码不正确"))
///* 326:    */       {
///* 327:327 */         rs.setState((byte)104);
///* 328:328 */         rs.setMsg(ans);
///* 329:    */       }
///* 330:329 */       else if (ans.contains("售票实行实名制"))
///* 331:    */       {
///* 332:330 */         rs.setState((byte)14);
///* 333:331 */         rs.setMsg(ans);
///* 334:    */       }
///* 335:332 */       else if (ans.contains("号码输入有误"))
///* 336:    */       {
///* 337:333 */         rs.setState((byte)15);
///* 338:334 */         rs.setMsg(ans);
///* 339:    */       }
///* 340:    */       else
///* 341:    */       {
///* 342:336 */         rs.setState((byte)103);
///* 343:337 */         rs.setMsg(ans);
///* 344:    */       }
///* 345:340 */       this.log.debug(ans);
///* 346:    */     }
///* 347:    */     catch (Exception e)
///* 348:    */     {
///* 349:342 */       this.log.error(e, e);
///* 350:    */     }
///* 351:344 */     this.log.debug("-------------------submit order end-------------------");
///* 352:345 */     return rs;
///* 353:    */   }
///* 354:    */   
///* 355:    */   public List<TrainQueryInfo> queryTrain(String from, String to, String startDate, String rangDate)
///* 356:    */   {
///* 357:357 */     this.log.debug("-------------------query train start-------------------");
///* 358:358 */     if ((rangDate == null) || (rangDate.isEmpty())) {
///* 359:359 */       rangDate = "00:00--24:00";
///* 360:    */     }
///* 361:361 */     List<NameValuePair> parameters = new ArrayList();
///* 362:362 */     parameters.add(new BasicNameValuePair("method", "queryLeftTicket"));
///* 363:363 */     parameters.add(new BasicNameValuePair("includeStudent", "00"));
///* 364:364 */     parameters.add(new BasicNameValuePair("orderRequest.from_station_telecode", Util.getCityCode(from)));
///* 365:365 */     parameters.add(new BasicNameValuePair("orderRequest.start_time_str", rangDate));
///* 366:366 */     parameters.add(new BasicNameValuePair("orderRequest.to_station_telecode", Util.getCityCode(to)));
///* 367:367 */     parameters.add(new BasicNameValuePair("orderRequest.train_date", startDate));
///* 368:368 */     parameters.add(new BasicNameValuePair("orderRequest.train_no", ""));
///* 369:369 */     parameters.add(new BasicNameValuePair("seatTypeAndNum", ""));
///* 370:370 */     parameters.add(new BasicNameValuePair("trainClass", "QB#D#Z#T#K#QT#"));
///* 371:371 */     parameters.add(new BasicNameValuePair("trainPassType", "QB"));
///* 372:372 */     HttpGet get = new HttpGet(Constants.QUERY_TRAIN_URL + URLEncodedUtils.format(parameters, "UTF-8"));
///* 373:373 */     ResponseHandler<String> responseHandler = new BasicResponseHandler();
///* 374:374 */     String responseBody = null;
///* 375:375 */     List<TrainQueryInfo> all = new ArrayList();
///* 376:    */     try
///* 377:    */     {
///* 378:377 */       responseBody = (String)this.httpclient.execute(get, responseHandler);
///* 379:378 */       all = Util.parserQueryInfo(responseBody, startDate);
///* 380:    */     }
///* 381:    */     catch (Exception e)
///* 382:    */     {
///* 383:383 */       this.log.error(e);
///* 384:    */     }
///* 385:385 */     this.log.debug("-------------------query train end-------------------");
///* 386:386 */     return all;
///* 387:    */   }
///* 388:    */   
///* 389:    */   public Result login(String username, String password, String randCode, String randstr)
///* 390:    */   {
///* 391:399 */     this.log.debug("-----------------login start-----------------------");
///* 392:400 */     Result rs = new Result();
///* 393:401 */     HttpPost httppost = new HttpPost(Constants.LOGIN_URL);
///* 394:402 */     List<NameValuePair> parameters = new ArrayList();
///* 395:403 */     parameters.add(new BasicNameValuePair("method", "login"));
///* 396:404 */     parameters.add(new BasicNameValuePair("loginRand", randstr));
///* 397:405 */     parameters.add(new BasicNameValuePair("refundLogin", "N"));
///* 398:406 */     parameters.add(new BasicNameValuePair("refundFlag", "Y"));
///* 399:407 */     parameters.add(new BasicNameValuePair("loginUser.user_name", username));
///* 400:408 */     parameters.add(new BasicNameValuePair("nameErrorFocus", ""));
///* 401:409 */     parameters.add(new BasicNameValuePair("user.password", password));
///* 402:410 */     parameters.add(new BasicNameValuePair("passwordErrorFocus", ""));
///* 403:411 */     parameters.add(new BasicNameValuePair("randCode", randCode));
///* 404:412 */     parameters.add(new BasicNameValuePair("randErrorFocus", ""));
///* 405:413 */     String responseBody = null;
///* 406:    */     try
///* 407:    */     {
///* 408:415 */       UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, "ASCII");
///* 409:416 */       httppost.setEntity(uef);
///* 410:417 */       ResponseHandler<String> responseHandler = new BasicResponseHandler();
///* 411:418 */       responseBody = (String)this.httpclient.execute(httppost, responseHandler);
///* 412:419 */       String info = Util.removeTagFromHtml(responseBody);
///* 413:420 */       this.log.debug("-----------------------------------------------------");
///* 414:421 */       this.log.debug(info);
///* 415:422 */       this.log.debug("-----------------------------------------------------\n\n\n\n\n");
///* 416:436 */       if (responseBody.contains("登录名不存在"))
///* 417:    */       {
///* 418:437 */         this.log.error("用户:" + username + "登录名不存在");
///* 419:438 */         rs.setState((byte)111);
///* 420:439 */         rs.setMsg("登录名不存在");
///* 421:    */       }
///* 422:440 */       else if (info.contains("密码输入错误"))
///* 423:    */       {
///* 424:441 */         this.log.error("用户:" + username + "密码输入错误");
///* 425:442 */         rs.setState((byte)112);
///* 426:443 */         rs.setMsg("密码输入错误");
///* 427:    */       }
///* 428:444 */       else if (info.contains("密码修改"))
///* 429:    */       {
///* 430:445 */         int index = responseBody.indexOf("-->");
///* 431:446 */         this.log.debug(responseBody.substring(index + 4));
///* 432:447 */         rs.setState((byte)100);
///* 433:448 */         rs.setMsg("登录成功");
///* 434:    */         
///* 435:    */ 
///* 436:451 */         List<Cookie> cookies = this.httpclient.getCookieStore().getCookies();
///* 437:452 */         for (Cookie cookie : cookies)
///* 438:    */         {
///* 439:453 */           String name = cookie.getName();
///* 440:454 */           if ("JSESSIONID".equals(name)) {
///* 441:455 */             JSESSIONID = cookie.getValue();
///* 442:456 */           } else if ("BIGipServerotsweb".equals(name)) {
///* 443:457 */             BIGipServerotsweb = cookie.getValue();
///* 444:    */           }
///* 445:    */         }
///* 446:460 */         System.out.println("JSESSIONID=" + JSESSIONID + ",BIGipServerotsweb=" + BIGipServerotsweb);
///* 447:    */       }
///* 448:461 */       else if (info.contains("请输入正确的验证码"))
///* 449:    */       {
///* 450:462 */         this.log.warn("用户:" + username + "请输入正确的验证码");
///* 451:463 */         rs.setState((byte)104);
///* 452:464 */         rs.setMsg("请输入正确的验证码");
///* 453:    */       }
///* 454:465 */       else if (responseBody.contains("<title>登录</title>"))
///* 455:    */       {
///* 456:466 */         this.log.info("用户:" + username + "当前访问用户过多,请重新登录");
///* 457:467 */         rs.setState((byte)113);
///* 458:468 */         rs.setMsg("当前访问用户过多,请重新登录");
///* 459:    */       }
///* 460:469 */       else if (responseBody.contains("当前访问用户过多"))
///* 461:    */       {
///* 462:470 */         this.log.info("用户:" + username + "当前访问用户过多");
///* 463:471 */         rs.setState((byte)114);
///* 464:472 */         rs.setMsg("当前访问用户过多");
///* 465:    */       }
///* 466:473 */       else if (responseBody.contains("已经被锁定"))
///* 467:    */       {
///* 468:474 */         this.log.info("用户:" + username + "已经被锁定");
///* 469:475 */         rs.setState((byte)115);
///* 470:476 */         rs.setMsg("已经被锁定");
///* 471:    */       }
///* 472:477 */       else if (responseBody.contains("系统维护中"))
///* 473:    */       {
///* 474:478 */         this.log.info("用户:" + username + "系统维护中");
///* 475:479 */         rs.setState((byte)116);
///* 476:480 */         rs.setMsg("系统维护中");
///* 477:    */       }
///* 478:    */       else
///* 479:    */       {
///* 480:482 */         this.log.info("用户:" + username + "未知错误");
///* 481:483 */         rs.setState((byte)103);
///* 482:484 */         rs.setMsg("未知错误");
///* 483:485 */         System.out.println(responseBody);
///* 484:    */       }
///* 485:    */     }
///* 486:    */     catch (Exception e)
///* 487:    */     {
///* 488:488 */       this.log.error(e);
///* 489:    */     }
///* 490:490 */     this.log.debug("-------------------login end---------------------");
///* 491:491 */     return rs;
///* 492:    */   }
///* 493:    */   
///* 494:    */   public Result queryOrder()
///* 495:    */   {
///* 496:502 */     this.log.debug("-------------------query order start-------------------");
///* 497:503 */     Result rs = new Result();
///* 498:504 */     HttpGet httpget = new HttpGet(Constants.QUERY_ORDER_URL);
///* 499:505 */     StringBuilder responseBody = new StringBuilder();
///* 500:506 */     BufferedReader br = null;
///* 501:    */     try
///* 502:    */     {
///* 503:508 */       HttpResponse response = this.httpclient.execute(httpget);
///* 504:509 */       HttpEntity entity = response.getEntity();
///* 505:510 */       br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
///* 506:    */       
///* 507:512 */       String line = null;
///* 508:513 */       while ((line = br.readLine()) != null) {
///* 509:514 */         responseBody.append(line);
///* 510:    */       }
///* 511:516 */       String msg = Util.removeTagFromHtml(responseBody.toString());
///* 512:518 */       if (!msg.isEmpty())
///* 513:    */       {
///* 514:519 */         int index = msg.indexOf("-->");
///* 515:520 */         msg = msg.substring(index + 4);
///* 516:521 */         String[] allInfo = msg.split("！");
///* 517:522 */         if (allInfo.length > 1)
///* 518:    */         {
///* 519:523 */           String usefulInfo = allInfo[1];
///* 520:525 */           if (usefulInfo.contains("待支付"))
///* 521:    */           {
///* 522:526 */             rs.setState((byte)12);
///* 523:527 */             rs.setMsg(usefulInfo);
///* 524:    */           }
///* 525:528 */           else if (usefulInfo.contains("取消次数过多"))
///* 526:    */           {
///* 527:529 */             rs.setState((byte)13);
///* 528:530 */             rs.setMsg(usefulInfo);
///* 529:    */           }
///* 530:    */           else
///* 531:    */           {
///* 532:532 */             rs.setMsg(usefulInfo);
///* 533:    */           }
///* 534:    */         }
///* 535:    */         else
///* 536:    */         {
///* 537:535 */           rs.setState((byte)11);
///* 538:536 */           rs.setMsg(msg);
///* 539:    */         }
///* 540:    */       }
///* 541:    */       else
///* 542:    */       {
///* 543:539 */         rs.setMsg(msg);
///* 544:    */       }
///* 545:    */       try
///* 546:    */       {
///* 547:545 */         if (br != null) {
///* 548:546 */           br.close();
///* 549:    */         }
///* 550:    */       }
///* 551:    */       catch (IOException e)
///* 552:    */       {
///* 553:549 */         e.printStackTrace();
///* 554:    */       }
///* 555:552 */       this.log.debug("-------------------query order end---------------------");
///* 556:    */     }
///* 557:    */     catch (Exception e)
///* 558:    */     {
///* 559:542 */       this.log.error(e);
///* 560:    */     }
///* 561:    */     finally
///* 562:    */     {
///* 563:    */       try
///* 564:    */       {
///* 565:545 */         if (br != null) {
///* 566:546 */           br.close();
///* 567:    */         }
///* 568:    */       }
///* 569:    */       catch (IOException e)
///* 570:    */       {
///* 571:549 */         e.printStackTrace();
///* 572:    */       }
///* 573:    */     }
///* 574:553 */     return rs;
///* 575:    */   }
///* 576:    */   
///* 577:    */   String getCode(String url)
///* 578:    */     throws IOException
///* 579:    */   {
///* 580:564 */     JFrame frame = new JFrame("验证码");
///* 581:565 */     JLabel label = new JLabel(new ImageIcon(getCodeByte(url)), 0);
///* 582:    */     
///* 583:567 */     frame.add(label);
///* 584:568 */     frame.setDefaultCloseOperation(3);
///* 585:569 */     frame.setSize(300, 200);
///* 586:570 */     frame.setLocationRelativeTo(null);
///* 587:571 */     frame.setVisible(true);
///* 588:572 */     InputStreamReader isr = new InputStreamReader(System.in);
///* 589:573 */     BufferedReader br = new BufferedReader(isr);
///* 590:574 */     String rd = br.readLine();
///* 591:575 */     frame.dispose();
///* 592:576 */     return rd;
///* 593:    */   }
/* 594:    */   
/* 595:    */   public byte[] getCodeByte(String url)
/* 596:    */   {
/* 597:586 */     this.log.debug("-------------------get randcode start-------------------");
/* 598:587 */     HttpGet get = new HttpGet(url);
/* 599:588 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 600:    */     try
/* 601:    */     {
/* 602:590 */       HttpResponse response = this.httpclient.execute(get);
/* 603:    */       
/* 604:592 */       HttpEntity entity = response.getEntity();
/* 605:593 */       this.log.debug(response.getStatusLine());
/* 606:594 */       if (entity != null)
/* 607:    */       {
/* 608:595 */         InputStream is = entity.getContent();
/* 609:596 */         byte[] buf = new byte[1024];
/* 610:597 */         int len = -1;
/* 611:598 */         while ((len = is.read(buf)) > -1) {
/* 612:599 */           baos.write(buf, 0, len);
/* 613:    */         }
/* 614:    */       }
/* 615:    */     }
/* 616:    */     catch (Exception e)
/* 617:    */     {
/* 618:603 */       this.log.error(e);
/* 619:    */     }
/* 620:605 */     this.log.debug("-------------------get randcode end-------------------");
/* 621:606 */     return baos.toByteArray();
/* 622:    */   }
/* 623:    */   
/* 624:    */   public String getStr(String url)
/* 625:    */   {
/* 626:614 */     this.log.debug("-------------------get randstr start-------------------");
/* 627:615 */     String s = "";
/* 628:616 */     HttpGet get = new HttpGet(url);
/* 629:617 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 630:    */     try
/* 631:    */     {
/* 632:619 */       HttpResponse response = this.httpclient.execute(get);
/* 633:    */       
/* 634:621 */       HttpEntity entity = response.getEntity();
/* 635:622 */       this.log.debug(response.getStatusLine());
/* 636:623 */       if (entity != null)
/* 637:    */       {
/* 638:624 */         InputStream is = entity.getContent();
/* 639:625 */         byte[] buf = new byte[1024];
/* 640:626 */         int len = -1;
/* 641:627 */         while ((len = is.read(buf)) > -1) {
/* 642:628 */           baos.write(buf, 0, len);
/* 643:    */         }
/* 644:    */       }
/* 645:    */     }
/* 646:    */     catch (Exception e)
/* 647:    */     {
/* 648:632 */       this.log.error(e);
/* 649:    */     }
/* 650:634 */     s = baos.toString();
/* 651:    */     
/* 652:636 */     this.log.debug("-------------------get randstr end-------------------" + s.toString());
///* 653:637 */     String randstr = s;
/* 654:    */     
/* 655:639 */     return s;
/* 656:    */   }
/* 657:    */ }


/* Location:           C:\Users\Administrator\Desktop\GoHome_2013版\original-ticketRobot-0.0.1-SNAPSHOT.jar
 * Qualified Name:     com.ywh.train.logic.TrainClient
 * JD-Core Version:    0.7.0.1
 */