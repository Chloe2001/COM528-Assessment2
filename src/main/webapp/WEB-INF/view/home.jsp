<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page import="org.solent.com504.oodd.cart.model.service.ShoppingCart"%>
<%@page import="org.solent.com504.oodd.cart.web.WebObjectFactory"%>
<%@page import="org.solent.com504.oodd.cart.model.service.ShoppingService"%>
<%@page import="org.solent.com504.oodd.cart.model.dto.ShoppingItem"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//request.setAttribute("selectedPage", "home");
 String message="";

    ShoppingService shoppingService = WebObjectFactory.getShoppingService();

    ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
    if (shoppingCart == null) {
        shoppingCart = WebObjectFactory.getNewShoppingCart();
        session.setAttribute("shoppingCart", shoppingCart);
    }

    String action = (String) request.getParameter("action");
    String itemName = (String) request.getParameter("itemName");
    String itemUuid = (String) request.getParameter("itemUUID");

    if ("addItemToCart".equals(action)) {
        ShoppingItem shoppingItem = shoppingService.getNewItemByName(itemName);
        if (shoppingItem == null) {
            message = "cannot add unknown " + itemName + " to cart";
        } else {
            message = "adding " + itemName + " to cart price= " + shoppingItem.getPrice();
            shoppingCart.addItemToCart(shoppingItem);
        }
    } else if ("removeItemFromCart".equals(action)) {
        message = "removed " + itemName + " from cart";
        shoppingCart.removeItemFromCart(itemUuid);
    } else {
        message = "unknown action=" + action;
    }
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Home</H1>
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>

    <H1>Available Items</H1>
    <table class="table">

        <tr>
            <th>Item Name</th>
            <th>Price</th>
            <th></th>
        </tr>

        <c:forEach var="item" items="${availableItems}">

            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td></td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home" method="get">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="addItemToCart">
                        <button type="submit" >Add Item</button>
                    </form> 
                </td>
            </tr>

        </c:forEach>
    </table>

    <H1>Shopping cart</H1>
    <table>

            <tr>
                <th>Item Name</th>
                <th>Price</th>
                <th>Quantity</th>
            </tr>

            <% for (ShoppingItem item : shoppingCart.getShoppingCartItems()) {%>
            <tr>
                <td><%=item.getName()%></td>
                <td><%=item.getPrice()%></td>
                <td><%=item.getQuantity()%></td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home.jsp" method="post">
                        <input type="hidden" name="itemUUID" value="<%=item.getUuid()%>">
                        <input type="hidden" name="itemName" value="<%=item.getName()%>">
                        <input type="hidden" name="action" value="removeItemFromCart">
                        <button type="submit" >Remove Item</button>
                    </form> 
                </td>
            </tr>
            <% }%>
            <tr>
                <td>TOTAL</td>
                <td><%=shoppingCart.getTotal()%></td>
            </tr>
        </table>



</main>
<jsp:include page="footer.jsp" />
