import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { formatFileSize } from 'app/config/constants';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFile } from 'app/shared/model/file.model';
import { getEntities } from 'app/entities/file/file.reducer';

export const File = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const fileList = useAppSelector(state => state.file.entities);
  const loading = useAppSelector(state => state.file.loading);
  const totalItems = useAppSelector(state => state.file.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getMediaTypeIcon = typeName => {
    switch (typeName) {
      case 'image':
        return 'png.png';
      case 'docx':
        return 'doc.png';
      case 'doc':
        return 'doc.png';
      case 'csv':
        return 'csv.png';
      case 'pdf':
        return 'pdf.png';
      case 'mp4':
        return 'mp4.png';
      default:
        return 'default.png'; // fallback icon
    }
  };

  return (
    <div>
      <div>
        <h2 id="file-heading" data-cy="FileHeading">
          Files
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} /> Refresh
            </Button>
            <Link to="/file/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Add File
            </Link>
          </div>
        </h2>
        <div className="table-responsive">
          {fileList && fileList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>Media</th>
                  <th>Folder</th>
                  <th className="hand" onClick={sort('name')}>
                    Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('size')}>
                    Size <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('added')}>
                    Added <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {fileList.map((file, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <img
                        src={`content/images/mediatypes/${getMediaTypeIcon(file.typeName)}`}
                        alt={file.typeName}
                        style={{ width: '24px', height: '24px' }}
                      />
                    </td>
                    <td>{file.foldername ? <Link to={`/folder/${file.folderId}`}>{file.foldername}</Link> : ''}</td>
                    <td>{file.name}</td>
                    <td>{formatFileSize(file.size)}</td>
                    <td>{file.added ? <TextFormat type="date" value={file.added} format={APP_DATE_FORMAT} /> : null}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/file/${file.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Files found</div>
          )}
        </div>
        {totalItems ? (
          <div className={fileList && fileList.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={paginationState.activePage}
                onSelect={handlePagination}
                maxButtons={5}
                itemsPerPage={paginationState.itemsPerPage}
                totalItems={totalItems}
              />
            </div>
          </div>
        ) : (
          ''
        )}
      </div>
    </div>
  );
};

export default File;
