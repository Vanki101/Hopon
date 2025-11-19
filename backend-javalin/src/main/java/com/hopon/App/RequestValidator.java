package com.hopon.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.Context;

/**
 *Validerer innkommende HTTP-parametere.
 * Brukes i App.java for å holde rutene rene.
 */

public class RequestValidator {

    private static final Logger log = LoggerFactory.getLogger(RequestValidator.class);

    /**
     * Sørger for at minst én av query-parametrene "name" eller "id" er satt.
     * Returnerer true hvis gyldig, false hvis feiler (og setter HTTP-status + melding).
     */
public static boolean validateNameOrId(Context ctx) {
    String name = ctx.queryParam("name");
    String id = ctx.queryParam("id");

        if ((name == null || name.isEmpty()) && (id == null || id.isEmpty())) {
            log.warn("Missing query parameter 'name' or 'id'");
            ctx.status(400).result("Query parameter 'name' or 'id' is required");
            return false;
        }
        return true;
    }

    /**
     * Validerer at en gitt query-param ikke er tom.
     */
  public static boolean requireQueryParam(Context ctx, String paramName) {
      String value = ctx.queryParam(paramName);
      if (value == null || value.isEmpty()) {
          log.warn("Missing query parameter '{}'", paramName);
          ctx.status(400).result("Query parameter '" + paramName + "' is required");
          return false;}
      return true;}

    /**
     * Validerer at request body ikke er tom (for POST-endepunkt).
     */
  public static boolean requireBody(Context ctx) {
      String body = ctx.body();
      if (body == null || body.isEmpty()) {
          log.warn("Missing request body");
          ctx.status(400).result("Request body is required");
          return false;}
      return true;}

    /**     
    * Validerer at både 'from' og 'to' parametere er satt.
    * Returnerer true hvis begge er gyldige, false hvis noen mangler.
    */
public static boolean requireFromAndTo(Context ctx) {
    String from = ctx.queryParam("from");
    String to = ctx.queryParam("to");

        if (from == null || to == null) {
            log.warn("Missing query parameters 'from' and/or 'to'");
            ctx.status(400).result("Query params 'from' and 'to' are required");
            return false;
        }
        return true;
    }


/**   
* Henter enten 'id' eller 'name' fra query params.
* Returnerer id hvis satt, ellers name.
*/

public static String getStopInput(Context ctx) {
    String id = ctx.queryParam("id");
    String name = ctx.queryParam("name");
    return (id != null && !id.isEmpty()) ? id : name;}
}