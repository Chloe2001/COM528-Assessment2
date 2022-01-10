<%-- 
    Document   : newItem
    Created on : 10 Jan 2022, 10:46:20
    Author     : chase
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<main role=""main" class=""container">
    
 <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span>
            </button>
                ${errorMessage}
        </div>
    </c:if>
    <%-- handle message --%>

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span>
            </button>
                ${message}
        </div>
    </c:if>
        <H1> Create a new item </H1>
        <form action="./viewCreateItem" method="POST" enctype ="multipart/form-data">
            <table class=""table>
                <tbody>
                    <tr>
                        <td>Item Name</td>
                        <td><input type="text" name="Item name" value="" required/></td>
                    </tr>
                    <tr>Item Price</tr>
                 <td><input type="text" name="Item price" value="" required/></td>
                </tr>
                <tr>
                    <td>Item Quantity</td>
                    <td><input type="text" name="Item quantity" value="" required/></td>
                </tr>
                </tbody>
            </table>
            
            <input type="hidden" name="action" value="create"/>
            <button class="btn" type="submit">Create Item</button>
        </form>

        <form action="./catalog">
            <button class="btn" type="submit">Return To Catalogue</button>
        </form>
</main>
        
        <jsp:include page="footer.jsp"/>