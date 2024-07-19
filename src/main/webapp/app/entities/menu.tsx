import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/filetype">
        Filetype
      </MenuItem>
      <MenuItem icon="asterisk" to="/folder">
        Folder
      </MenuItem>
      <MenuItem icon="asterisk" to="/file">
        File
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
