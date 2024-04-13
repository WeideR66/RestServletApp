## RestServletApp - Домашняя работа

- API работает с сущностью User, позволяет создать/достать/обновить/удалить запись об пользователе.
- User связан с сущностью Address по One-To-One и с сущностью BankAccount по One-To-Many.

Таблица Users
<table>
<tr>
<th>users</th>
</tr>
<tr>
<td>id</td>
</tr>
<tr>
<td>name</td>
</tr>
<tr>
<td>surname</td>
</tr>
<tr>
<td>age</td>
</tr>
</table>

Таблица Addresses
<table>
<tr>
<th>addresses</th>
</tr>
<tr>
<td>id</td>
</tr>
<tr>
<td>country</td>
</tr>
<tr>
<td>city</td>
</tr>
<tr>
<td>street</td>
</tr>
<tr>
<td>number</td>
</tr>
<tr>
<td>user_id</td>
</tr>
</table>

Таблица BankAccounts
<table>
<tr>
<th>bankaccounts</th>
</tr>
<tr>
<td>id</td>
</tr>
<tr>
<td>accountname</td>
</tr>
<tr>
<td>cash</td>
</tr>
<tr>
<td>user_id</td>
</tr>
</table>

- [Ссылка на видео с демонстрацией работы API](https://disk.yandex.ru/i/QsavAGnQLqEAaQ)
- Использовал: JDBC, Jakarta ServletAPI, Lombok, Jackson, Junit, PostgreSQL. 