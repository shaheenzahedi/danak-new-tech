import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/child">
        Child
      </MenuItem>
      <MenuItem icon="asterisk" to="/device">
        Device
      </MenuItem>
      <MenuItem icon="asterisk" to="/facilitator">
        Facilitator
      </MenuItem>
      <MenuItem icon="asterisk" to="/centre">
        Centre
      </MenuItem>
      <MenuItem icon="asterisk" to="/facilitator-centre-association">
        Facilitator Centre Association
      </MenuItem>
      <MenuItem icon="asterisk" to="/province">
        Province
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        City
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        Country
      </MenuItem>
      <MenuItem icon="asterisk" to="/progress">
        Progress
      </MenuItem>
      <MenuItem icon="asterisk" to="/unit-list">
        Unit List
      </MenuItem>
      <MenuItem icon="asterisk" to="/single-unit">
        Single Unit
      </MenuItem>
      <MenuItem icon="asterisk" to="/unit-config">
        Unit Config
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
