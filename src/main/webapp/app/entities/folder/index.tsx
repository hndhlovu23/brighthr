import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Folder from './folder';
import FolderDetail from './folder-detail';
import FolderUpdate from './folder-update';
import FolderDeleteDialog from './folder-delete-dialog';

const FolderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Folder />} />
    <Route path="new" element={<FolderUpdate />} />
    <Route path=":id">
      <Route index element={<FolderDetail />} />
      <Route path="edit" element={<FolderUpdate />} />
      <Route path="delete" element={<FolderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FolderRoutes;
