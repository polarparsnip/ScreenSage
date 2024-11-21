/* eslint-disable @typescript-eslint/no-explicit-any */
import { Clear, ThumbUp, ThumbUpOutlined } from '@mui/icons-material';
import s from './ListCard.module.scss'
import { Link } from 'react-router-dom'
import { Grow, Tooltip } from '@mui/material';

export function ListCard({ list, author, listType, onRemove, i }: { list: any; author: boolean; listType: string; onRemove: (id: number) => void; i: number; }) {
  const date = new Date(list.createdAt);
  const formattedDate = date.toLocaleString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <Grow in key={i} timeout={(i + 1) * 250}>
      <Link to={`/${list.watchlist ? 'watchlists' : 'lists'}/${list.id}`}>
        <div className={s.list_card}>
          {author && listType != 'lists' &&
            <Tooltip disableTouchListener title={'Delete list'}>
              <button 
                className={s.list_card__remove} 
                onClick={(event) => {
                  event.preventDefault();
                  onRemove(list.id)
              }}>
                <Clear/>
              </button>
            </Tooltip>
          }
          {list.mediaListItems.length == 1 && 
            <img className={s.solo_image} src={list.mediaListItems[0].mediaImg} alt={'List image'} />
          }
          {list.mediaListItems.length > 1 && 
            <div className={s.image_stack}>
              <img className={s.image} src={list.mediaListItems[0].mediaImg} alt={'List image'} />
              <img className={s.image} src={list.mediaListItems[1].mediaImg} alt={'List image'} />
              {list.mediaListItems.length > 2 && <img className={s.image} src={list.mediaListItems[2].mediaImg} alt={'List image'} />}
            </div>
          }
          <div className={s.list_card__card_content}>
          <div className={s.list_card__text_content}>
            <div>Created by: {list.user?.username ? list.user.username : 'Unknown'} on {formattedDate}</div>
            <h1>{list.title}</h1>
            <p>
              {list.description}
            </p>
            <div className={s.items_likes_container}>
              <div>
                Media items in list: {list.mediaListItems.length}
              </div>
              {listType != 'watchlists' && list.mediaListItems.length > 0 && <div
                className={s.user_likes} 
              >
                {list.userHasLiked ? (
                  <ThumbUp className={s.icon} />
                ) : (
                  <ThumbUpOutlined className={s.icon} />
                )}
                <span className={s.count}>{list.likeCount}</span>
              </div>}
            </div>
          </div>
          </div>
        </div>
      </Link>
    </Grow>
  )
}
