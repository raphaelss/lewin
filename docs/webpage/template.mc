<%class>
  has 'description';
  has 'short_lang_name';
  has 'subtitle';
  has 'description';
  has 'supported_operations_title';
  has 'supported_operations';
  has 'download_title';
  has 'download_text';
  has 'contributing_title';
  has 'contributing_text';
  has 'authors_title';
  has 'authors_text';
  has 'license_title';
  has 'lang_links';
</%class>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <link rel="stylesheet" href="style.css" type="text/css" media="all"/>
  <title>Lewin - <% $.subtitle %></title>
</head>
<body>
  <header>
    <ul class="lang-menu">
% foreach (@{$.lang_links}) {
      <li class="lang-menu"><a href="<% $_->{link} %>"><% $_->{short_name} %></a></li>
% }
    </ul>
  </header>
  <div class="section">
    <h1>Lewin</h1>
    <p><% $.description %></p>
    <div class="subsection">
      <h2><% $.supported_operations_title %></h2>
      <ul class="list-hyph">
% foreach (@{$.supported_operations}) {
        <li class="list-hyph"><% $_ %></li>
% }
      </ul>
    </div>
  </div>
  <div class="section">
    <h1><% $.download_title %></h1>
    <p><% $.download_text %></p>
    <ul class="list-hyph">
      <li class="list-hyph"><a class="link-ext" href="http://lewin.raphaelss.com/lewin-1.0.2.zip">Lewin-1.0.2.zip</a></li>
      <li class="list-hyph"><a class="link-ext" href="http://lewin.raphaelss.com/lewin-1.0.1.zip">Lewin-1.0.1.zip</a></li>
    </ul>
  </div>
  <div class="section">
    <h1><% $.contributing_title %></h1>
    <p><% $.contributing_text %></p>
  </div>
  <div class="section">
    <h1><% $.authors_title %></h1>
    <p><% $.authors_text %></p>
  </div>
  <div class="section">
    <h1><% $.license_title %></h1>
    <p>Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino {at} gmail {dot} com</p>
    <p>Copyright (C) 2013 Liduino Jos&#233; Pitombeira de Oliveira, <a class="link-ext" href="http://www.pitombeira.com">http://www.pitombeira.com</a><p>
    <p>Copyright (C) 2015 Raphael Sousa Santos, <a class="link-ext" href="http://www.raphaelss.com">http://www.raphaelss.com</a></p>
    <p>This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.</p>
    <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.</p>
    <p>You should have received a copy of the GNU General Public License along with this program.  If not, see <a class="link-ext" href="http://www.gnu.org/licenses/.">http://www.gnu.org/licenses/.</a></p>
  </div>
</body>
</html>
