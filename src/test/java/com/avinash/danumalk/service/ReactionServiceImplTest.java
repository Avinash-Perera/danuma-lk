package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Like;
import com.avinash.danumalk.repository.ReactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReactionServiceImplTest {
    @InjectMocks
    private ReactionServiceImpl reactionService;

    @Mock
    private ReactionRepository reactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for adding a valid reaction and returning the saved reaction.
     *
     * @param  @paramName  Unused parameter
     * @return           The saved reaction
     */
    @Test
    void addReaction_ValidReaction_ReturnsSavedReaction() {
        // Arrange
        Like like = new Like();

        when(reactionRepository.save(like)).thenReturn(like);

        // Act
        Like savedLike = (Like) reactionService.addReaction(like);

        // Assert
        assertEquals(like, savedLike);
        verify(reactionRepository, times(1)).save(like);
    }

    /**
     * A test case to verify the functionality of the `removeReaction` method.
     *
     * @param  @reactionId  the reaction ID to be removed
     * @return             no return value
     */
    @Test
    void removeReaction_ValidReactionId_RemovesReaction() {
        // Arrange
        Long reactionId = 1L;

        // Act
        reactionService.removeReaction(reactionId);

        // Assert
        verify(reactionRepository, times(1)).deleteById(reactionId);
    }

    /**
     * A test case to verify the functionality of getTotalLikesForPost method.
     *
     * @param  @postId  The ID of the post for which to get the total likes.
     * @return         The total number of likes for the given post.
     */
    @Test
    void getTotalLikesForPost_ValidPostId_ReturnsTotalLikes() {
        // Arrange
        Long postId = 1L;
        int expectedTotalLikes = 5;

        when(reactionRepository.countLikesByPostId(postId)).thenReturn(expectedTotalLikes);

        // Act
        int totalLikes = reactionService.getTotalLikesForPost(postId);

        // Assert
        assertEquals(expectedTotalLikes, totalLikes);
        verify(reactionRepository, times(1)).countLikesByPostId(postId);
    }

    /**
     * Test case for the getTotalDislikesForPost method.
     *
     * @param  @postId  The ID of the post to get the total dislikes for.
     * @return         The total number of dislikes for the post.
     */
    @Test
    void getTotalDislikesForPost_ValidPostId_ReturnsTotalDislikes() {
        // Arrange
        Long postId = 1L;
        int expectedTotalDislikes = 3;

        when(reactionRepository.countDislikesByPostId(postId)).thenReturn(expectedTotalDislikes);

        // Act
        int totalDislikes = reactionService.getTotalDislikesForPost(postId);

        // Assert
        assertEquals(expectedTotalDislikes, totalDislikes);
        verify(reactionRepository, times(1)).countDislikesByPostId(postId);
    }





}
