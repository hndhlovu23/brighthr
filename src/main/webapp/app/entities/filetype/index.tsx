import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Filetype from './filetype';
import FiletypeDetail from './filetype-detail';
import FiletypeUpdate from './filetype-update';
import FiletypeDeleteDialog from './filetype-delete-dialog';

const FiletypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Filetype />} />
    <Route path="new" element={<FiletypeUpdate />} />
    <Route path=":id">
      <Route index element={<FiletypeDetail />} />
      <Route path="edit" element={<FiletypeUpdate />} />
      <Route path="delete" element={<FiletypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FiletypeRoutes;
